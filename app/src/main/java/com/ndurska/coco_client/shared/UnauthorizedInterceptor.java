package com.ndurska.coco_client.shared;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.ndurska.coco_client.shared.login.LoginFragment;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UnauthorizedInterceptor implements Interceptor, LoginFragment.LoginFragmentDismissListener {

    private Context context;
    private Handler handler;

    private static final Object lock = new Object();

    private static boolean isLoginFragmentShown = false;

    public UnauthorizedInterceptor(Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onLoginFragmentDismissed() {
        isLoginFragmentShown = false;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == 401 || response.code() == 403) {
            synchronized (lock) {
                if (!isLoginFragmentShown) {
                    showToastOnUIThread("Unauthorized!");
                    showLoginFragment();
                }
            }
        }
        return response;
    }

    private void showLoginFragment() {
        isLoginFragmentShown = true;
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            LoginFragment loginFragment = LoginFragment.newInstance();
            loginFragment.setLoginFragmentDismissListener(this);
            loginFragment.show(fragmentManager, "login_fragment");
        }
    }

    private void showToastOnUIThread(final String message) {
        handler.post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}