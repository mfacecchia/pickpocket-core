package com.feis.splitnings.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationMessage {
    REQUIRED_FIELD("This field is required"),
    NOT_EMPTY("This field cannot be empty"),
    NOT_BLANK("This field cannot be blank"),
    VALID_EMAIL("This field must contain a valid Email address");

    private final String message;
}
