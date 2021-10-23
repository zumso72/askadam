package com.adam.project.model.dto;

public class ChangeForgetPasswordDTO extends ChangePasswordDTO {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
