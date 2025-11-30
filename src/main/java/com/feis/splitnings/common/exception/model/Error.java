package com.feis.splitnings.common.exception.model;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {
    private final Integer errorCode;
    private final String errorMessage;
    @JsonIgnore
    private String devMessage;

    public Error(InternalErrorCode internalErrorCode, @Nullable String message, @Nullable String devMessage) {
        this.errorCode = internalErrorCode.getCode();
        this.errorMessage = message != null ? message : internalErrorCode.getMessage();
        this.devMessage = devMessage;
    }

    public Error(InternalErrorCode internalErrorCode, @Nullable String message) {
        this.errorCode = internalErrorCode.getCode();
        this.errorMessage = message != null ? message : internalErrorCode.getMessage();
        this.devMessage = null;
    }
}
