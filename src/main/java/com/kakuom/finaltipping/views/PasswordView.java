package com.kakuom.finaltipping.views;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordView {
    @NotBlank
    @Size(min = 5, max = 60)
    private String newPassword;

    public PasswordView() {
    }

    public PasswordView(@NotBlank @Size(min = 5, max = 60) String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
