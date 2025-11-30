package com.feis.splitnings.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.feis.splitnings.common.exception.model.Error;
import com.feis.splitnings.common.exception.model.InternalErrorCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ResponseStatus(code = HttpStatus.FORBIDDEN, value = HttpStatus.FORBIDDEN)
public class NotAvailableListException extends BaseException {

    public NotAvailableListException(Class<?> entityClass) {
        String message = String.format("No available %s found", entityClass.getSimpleName());
        Error error = new Error(InternalErrorCode.FORBIDDEN, message);
        this.getErrors().add(error);
    }
}
