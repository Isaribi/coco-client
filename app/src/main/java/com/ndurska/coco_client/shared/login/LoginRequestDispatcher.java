package com.ndurska.coco_client.shared.login;

import android.content.Context;

import com.ndurska.coco_client.shared.AbstractRequestDispatcher;
import com.ndurska.coco_client.shared.JsonConverter;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Request;

public class LoginRequestDispatcher extends AbstractRequestDispatcher {
    private final String url = "http://10.0.2.2:8080/api/auth";

    private final JsonConverter<SignInDto> signInJsonConverter;
    private final JsonConverter<TokenDto> tokenJsonConverter;
    public LoginRequestDispatcher(Context context) {
        super(context);
        signInJsonConverter = new JsonConverter<>(SignInDto.class);
        tokenJsonConverter = new JsonConverter<>(TokenDto.class);
    }


    public TokenDto signIn(SignInDto signInDto) throws IOException {

        Request request = new Request.Builder()
                .post(Objects.requireNonNull(signInJsonConverter.createJsonRequestBody(signInDto)))
                .url(url+"/sign-in")
                .build();

        String response = callRequestForBody(request);
//        if(response.code() == 403){
//            return null;
//        }

        return tokenJsonConverter.convertToObject(response);
    }

}
