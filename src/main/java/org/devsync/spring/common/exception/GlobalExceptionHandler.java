package org.devsync.spring.common.exception;

import org.devsync.spring.common.response.ApiError;
import org.devsync.spring.common.response.FieldValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        List<FieldValidationError> validationErrors = e.getBindingResult().getFieldErrors().stream().map(error -> FieldValidationError.builder().field(error.getField()).message(error.getDefaultMessage()).build()).toList();

        return buildResponse("Validation failed", errorCode, validationErrors);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return buildResponse("Bad request", errorCode, Collections.emptyList());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException e){
    return buildResponse(e.getMessage(),e.getErrorCode(),Collections.emptyList());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(
            NoResourceFoundException e) {

        log.warn("Resource not found: {}", e.getResourcePath());

        return buildResponse(
                "Requested resource not found",
                ErrorCode.NOT_FOUND,
                Collections.emptyList()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternalServerError(Exception e){
        log.error("Unexpected exception occurred", e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return buildResponse("Something went wrong",errorCode,Collections.emptyList());
    }


    private ResponseEntity<ApiError> buildResponse(String message, ErrorCode errorCode, List<FieldValidationError> errors) {
        return ResponseEntity.status(errorCode.getStatus()).body(ApiError.builder().timestamp(Instant.now()).errorCode(errorCode).errors(errors).message(message).build());
    }

}