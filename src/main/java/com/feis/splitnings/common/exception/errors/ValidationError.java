package com.feis.splitnings.common.exception.errors;

import com.feis.splitnings.common.exception.enums.InternalErrorCode;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationError extends Error {
    private String fieldName;

    public ValidationError(String fieldName, InternalErrorCode internalErrorCode, @Nullable String message, @Nullable String devMessage) {
        super(internalErrorCode, message, devMessage);
        this.fieldName = fieldName;
    }

    public ValidationError(String fieldName, InternalErrorCode internalErrorCode, @Nullable String message) {
        super(internalErrorCode, message);
        this.fieldName = fieldName;
    }
}
