package com.ndurska.coco_client.shared.login;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarActivity;
import com.ndurska.coco_client.shared.TokenHandler;

import java.io.IOException;

public class LoginFragment extends DialogFragment {

    private TextView etUsername;
    private TextView etPassword;
    private Button btnLogin;

    private Context context;

    private LoginRequestDispatcher loginRequestDispatcher;

    private LoginFragmentDismissListener dismissListener;

    public interface LoginFragmentDismissListener {
        void onLoginFragmentDismissed();
    }
    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        loginRequestDispatcher = new LoginRequestDispatcher(context);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        initViews();
        setListeners();
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onLoginFragmentDismissed();
        }
    }
    public void setLoginFragmentDismissListener(LoginFragmentDismissListener listener) {
        this.dismissListener = listener;
    }

    private void setListeners() {
        btnLogin.setOnClickListener(v -> {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    executorService.execute(() -> {
                        TokenDto tokenDto = null;
                        try {
                            tokenDto = loginRequestDispatcher.signIn(new SignInDto(username, password));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (tokenDto == null) {
                          getActivity().runOnUiThread(()-> {
                              etUsername.setError("Zły login lub hasło!");
                              etPassword.setError("Zły login lub hasło!");
                          });
                           return;
                       }
                        SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
                        editor.putString("jwtToken", tokenDto.getToken());
                        editor.apply();
                        TokenHandler.jwtToken = tokenDto.getToken();
                        if(context instanceof CalendarActivity){
                            CalendarActivity calendarActivity = (CalendarActivity) context;
                            calendarActivity.runOnUiThread(calendarActivity::refreshWeekView);
                        };
                        this.dismiss();
                    });

                }
        );
    }

    private void findViews(@NonNull View view) {
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
    }

    private void initViews() {


    }

}