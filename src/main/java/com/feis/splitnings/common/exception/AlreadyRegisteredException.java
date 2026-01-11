package com.feis.splitnings.common.exception;

import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.exception.errors.Error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, value = HttpStatus.CONFLICT)
public class AlreadyRegisteredException extends BaseException {

    public AlreadyRegisteredException() {
        Error error = new Error(InternalErrorCode.CONFLICT, "This user appears to be already registered");
        this.getErrors().add(error);
    }

    public AlreadyRegisteredException(String devMessage) {
        Error error = new Error(InternalErrorCode.CONFLICT, "This user appears to be already registered", devMessage);
        this.getErrors().add(error);
    }
}
