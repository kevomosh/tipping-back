package com.kakuom.finaltipping.views;

import javax.validation.constraints.NotBlank;

public class PlayerView {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    public PlayerView(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
