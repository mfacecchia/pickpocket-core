package com.feis.splitnings.common.exception;

import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.exception.errors.Error;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, value = HttpStatus.FORBIDDEN)
public class ForbiddenOperationException extends BaseException {

    public ForbiddenOperationException() {
        Error error = new Error(InternalErrorCode.FORBIDDEN, "Can't complete the request. Operation forbidden.");
        this.getErrors().add(error);
    }

    public ForbiddenOperationException(String message) {
        Error error = new Error(InternalErrorCode.FORBIDDEN, message);
        this.getErrors().add(error);
    }

    public ForbiddenOperationException(Error error) {
        super.addError(error);
    }

    public ForbiddenOperationException(List<Error> errors) {
        super.getErrors().addAll(errors);
    }
}
