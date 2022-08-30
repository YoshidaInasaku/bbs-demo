package com.yoshidainasaku.output.bbsdemo.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupForm {
    @NotBlank
    @Size(min = 1, max = 15, message = "Please enter between 1 and 15 characters")
    private String userId;
    @NotBlank
    @Size(min = 1, max = 24, message = "Please enter between 1 and 24 characters")
    private String userName;
    @NotBlank
    @Size(min = 1, max = 128, message = "Please enter between 1 and 128 characters")
    private String password;
    @NotBlank
    @Email(message = "Please enter the email address")
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
