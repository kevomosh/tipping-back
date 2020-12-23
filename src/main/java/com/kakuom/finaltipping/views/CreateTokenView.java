package com.kakuom.finaltipping.views;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateTokenView {
    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    public CreateTokenView() {
    }

    public CreateTokenView(@NotBlank @Size(max = 60) String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
