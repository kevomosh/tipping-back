package com.kakuom.finaltipping.views;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginView {
    @NotBlank(message = "Email is required")
    @Email
    @Size(max = 60)
    private String email;

    @NotBlank
    @Size(min = 5, max = 60)
    private String password;

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
