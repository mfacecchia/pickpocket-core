package com.feis.splitnings.common.exception;

import com.feis.splitnings.common.exception.model.Error;
import com.feis.splitnings.common.exception.model.InternalErrorCode;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.NOT_FOUND, value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String id) {
        String message = String.format("%s with id %s not found", resourceName, id);
        Error error = new Error(InternalErrorCode.RESOURCE_NOT_FOUND, message);
        this.getErrors().add(error);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String id) {
        String message = String.format("%s with %s %s not found", resourceName, fieldName, id);
        Error error = new Error(InternalErrorCode.RESOURCE_NOT_FOUND, message);
        this.getErrors().add(error);
    }
}
