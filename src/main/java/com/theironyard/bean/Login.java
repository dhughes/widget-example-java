package com.theironyard.bean;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by doug on 9/24/16.
 */
public class Login {

    @NotBlank
    public String email;

    @NotBlank
    public String password;

    public boolean loginFailed = false;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(boolean loginFailed) {
        this.loginFailed = loginFailed;
    }
}
