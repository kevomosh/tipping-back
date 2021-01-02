package com.kakuom.finaltipping.views;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class RegisterView {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank(message = "email is required")
    @Size(max = 60)
    private String email;

    @NotBlank
    @Size(min = 5, max = 60)
    private String password;

    private Set<Long> groupIds;

    public RegisterView(@NotBlank(message = "Name is required") @Size(min = 3, max = 50) String name,
                        @NotBlank(message = "email is required") @Size(max = 60) @Email String email,
                        @NotBlank @Size(min = 5, max = 60) String password, Set<Long> groupIds) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.groupIds = groupIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public Set<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Set<Long> groupIds) {
        this.groupIds = groupIds;
    }
}
