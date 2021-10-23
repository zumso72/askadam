package com.adam.project.model.dto;

import javax.validation.constraints.NotBlank;

public class ChangePasswordDTO {
    private String lastPassword;

    private String password;

    private String password2;

    public ChangePasswordDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getLastPassword() {
        return lastPassword;
    }

    public void setLastPassword(String lastPassword) {
        this.lastPassword = lastPassword;
    }
}
