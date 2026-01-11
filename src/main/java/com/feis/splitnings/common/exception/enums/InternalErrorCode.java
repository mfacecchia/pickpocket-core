package com.feis.splitnings.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalErrorCode {
    // 100 - 199 - Validation errors
    PARAMETER_REQUIRED(100, "Parameter required"),
    PARAMETER_INVALID(101, "Parameter invalid"),

    // 200 - 299 - Business errors
    CONFLICT(200, "Conflict"),

    // 300 - 399 - System errors

    // 400 - 499 - Resource errors
    RESOURCE_NOT_FOUND(400, "Resource not found"),
    UNAUTHORIZED(401, "Unauthorized"),
    FIELD_NOT_FOUND(402, "Field not found"),

    // 500 - 599 - Security errors
    INTERNAL_SEVER_ERROR(500, "Internal server error"),

    // 600 - 699 - Authentication errors

    // 700 - 799 - Authorization errors
    FORBIDDEN(700, "Access denied"),

    // 800 - X - Generic errors
    UNHANDLED_ERROR(800, "Unhandled error");

    private final int code;
    private final String message;
}
