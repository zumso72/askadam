package com.adam.project.model;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum Role implements GrantedAuthority, Serializable {
    ANSWERER,
    QUESTIONER;

    @Override
    public String getAuthority() {
        return name();
    }
}
