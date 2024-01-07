package com.ndurska.coco_client.shared.login;

import java.io.Serializable;

public class SignInDto implements Serializable {

    private String username;
    private String password;

    public SignInDto(){

    }

    public SignInDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
