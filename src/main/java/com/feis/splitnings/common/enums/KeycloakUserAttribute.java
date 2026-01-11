package com.feis.splitnings.common.enums;

import lombok.Getter;

@Getter
public enum KeycloakUserAttribute {
    USERNAME("username"),
    EMAIL("email"),
    USER_ID("userId"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    MIDDLE_NAME("middleName");

    private final String label;

    private KeycloakUserAttribute(String label) {
        this.label = label;
    }
}
