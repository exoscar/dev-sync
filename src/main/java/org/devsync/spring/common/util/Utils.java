package org.devsync.spring.common.util;

import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;

import java.util.UUID;

public class Utils {
    public  static UUID parseUuid(String id, String errorMessage) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(
                    errorMessage,
                    ErrorCode.BAD_REQUEST
            );
        }
    }
}
