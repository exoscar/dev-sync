package org.devsync.spring.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.devsync.spring.common.exception.ErrorCode;

import java.time.Instant;
import java.util.List;
import java.util.List;

@Data
@Builder
public class ApiError {
    private String message;
    private ErrorCode errorCode;
    private Instant timestamp;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldValidationError> errors;
}
