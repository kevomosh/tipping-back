package com.kakuom.finaltipping.views;

import javax.validation.constraints.Size;

public class ChangePasswordView {
    private String email;

    @Size(min = 5, max = 60)
    private String password;

    public ChangePasswordView() {
    }

    public ChangePasswordView(String email) {
        this.email = email;
    }


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
}
