package org.devsync.spring.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    WORKSPACE_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT),
    PROJECT_ALREADY_EXISTS(HttpStatus.CONFLICT),
    LABEL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    LABEL_ARCHIVED(HttpStatus.BAD_REQUEST),
    LABEL_ALREADY_ASSIGNED(HttpStatus.CONFLICT),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED);


    private final HttpStatus status;

    ErrorCode(HttpStatus status){
        this.status = status;
    }

}
