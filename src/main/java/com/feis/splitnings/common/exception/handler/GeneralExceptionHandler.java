package com.feis.splitnings.common.exception.handler;

import com.feis.splitnings.common.exception.BaseException;
import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.exception.data.dto.response.ErrorResponse;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.exception.errors.ValidationError;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GeneralExceptionHandler {
    private final static Logger logger = LogManager.getLogger(GeneralExceptionHandler.class);
    private static final String ERROR_MESSAGE_TEMPLATE = "%n Request uri: %s;%n Http status: %s;%n Http error: %s;%n Messages: %n %s.";
    private static final String LIST_JOIN_DELIMITER = "\n";

    /**
     * Exception thrown when the `@Valid` constraints validation is not satisfied
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidHandler(MethodArgumentNotValidException ex, WebRequest request) {
        List<Error> validationErrors = ex.getFieldErrors().stream()
                .map(
                        (fieldError) -> new ValidationError(
                                fieldError.getField(),
                                InternalErrorCode.PARAMETER_INVALID,
                                fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationException validationException = new ValidationException(validationErrors);
        validationException.setStackTrace(ex.getStackTrace());

        return generalExceptionHandler(validationException, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataIntegrityViolationHandler(DataIntegrityViolationException ex, WebRequest request) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getCause();

        Error error = new Error(InternalErrorCode.CONFLICT, constraintViolationException.getSQLException().getMessage(),
                constraintViolationException.getSQL());
        BaseException baseException = new BaseException();
        baseException.addError(error);
        baseException.setStackTrace(ex.getStackTrace());

        ErrorResponse errorResponse = buildErrorResponse(baseException, HttpStatus.CONFLICT, request);

        logError(errorResponse);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> authorizationDeniedHandler(AuthorizationDeniedException ex, WebRequest request) {
        InternalErrorCode internalErrorCode = InternalErrorCode.FORBIDDEN;

        Error error = new Error(internalErrorCode, internalErrorCode.getMessage(), ex.getMessage());

        BaseException baseException = new BaseException();
        baseException.addError(error);
        baseException.setStackTrace(ex.getStackTrace());

        ErrorResponse errorResponse = buildErrorResponse(baseException, HttpStatus.FORBIDDEN, request);

        logError(errorResponse);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<ErrorResponse> baseExceptionHandler(BaseException ex, WebRequest request) {
        HttpStatus status = getExceptionStatusCode(ex);

        ErrorResponse errorResponse = buildErrorResponse(ex, status, request);

        logError(errorResponse);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> generalExceptionHandler(Exception exception, WebRequest request) {
        BaseException baseException = new BaseException();

        if (exception instanceof BaseException) {
            baseException = (BaseException) exception;
        } else {
            Error error = new Error(InternalErrorCode.UNHANDLED_ERROR, "An unexpected error occurred while handling your request.");
            baseException.addError(error);
        }
        baseException.setStackTrace(exception.getStackTrace());

        final HttpStatus status = getExceptionStatusCode(exception);
        ErrorResponse errorResponse = buildErrorResponse(baseException, status, request);

        logError(errorResponse);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    public static ErrorResponse buildErrorResponse(BaseException baseException, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setPath(request.getDescription(false));
        errorResponse.setTimestamp(Instant.now());

        errorResponse.setStatus(httpStatus.value());
        errorResponse.setError(httpStatus.getReasonPhrase());

        errorResponse.setErrors(baseException.getErrors());
        return errorResponse;
    }

    private void logError(ErrorResponse response) {
        logger.error(buildErrorMessageTemplate(response));
    }

    private String buildErrorMessageTemplate(ErrorResponse response) {
        return buildErrorMessageTemplate(response.getPath(), response.getStatus(), response.getError(),
                response.getErrors());
    }

    private String buildErrorMessageTemplate(String requestUri, Integer httpStatus, String httpError, List<Error> errors) {
        String messages = "";

        if (errors != null && !errors.isEmpty()) {
            messages = messages.concat(
                    errors.stream()
                            .map(e -> {
                                return "Error code: " + e.getErrorCode() + " - Error Message: " + e.getErrorMessage()
                                        + " - Dev message: " + e.getDevMessage();
                            })
                            .collect(Collectors.joining(LIST_JOIN_DELIMITER)));
        }
        return new StringFormattedMessage(ERROR_MESSAGE_TEMPLATE, requestUri, httpStatus, httpError, messages) .toString();
    }

    private HttpStatus getExceptionStatusCode(Exception ex) {
        Class<? extends Exception> exceptionClass = ex.getClass();
        ResponseStatus responseStatus = exceptionClass.getAnnotation(ResponseStatus.class);
        final HttpStatus status = responseStatus != null ? responseStatus.code() : HttpStatus.INTERNAL_SERVER_ERROR;

        return status;
    }
}

