package com.feis.splitnings.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.feis.splitnings.common.exception.model.Error;
import com.feis.splitnings.common.exception.model.InternalErrorCode;

@ResponseStatus(code = HttpStatus.CONFLICT, value = HttpStatus.CONFLICT)
public class ConflictException extends BaseException {

    public ConflictException(String resourceName, String id) {
        String message = String.format("%s with id %s already existing", resourceName, id);
        Error error = new Error(InternalErrorCode.CONFLICT, message);
        this.getErrors().add(error);
    }

    public ConflictException(String resourceName, String fieldName, String id) {
        String message = String.format("%s with %s %s already existing", resourceName, fieldName, id);
        Error error = new Error(InternalErrorCode.CONFLICT, message);
        this.getErrors().add(error);
    }

    public ConflictException(Error error) {
        super.addError(error);
    }

    public ConflictException(List<Error> errors) {
        super.getErrors().addAll(errors);
    }
}
