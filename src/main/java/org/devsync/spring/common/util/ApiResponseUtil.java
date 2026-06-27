package org.devsync.spring.common.util;

import org.devsync.spring.common.response.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {
    public static <T> ApiResponse<T> success(T data, String message){
        return ApiResponse.<T>builder().data(data).message(message).success(true).build();
    }
    public static <T> ApiResponse<T> success(T data){
        return ApiResponse.<T>builder().data(data).message("success").success(true).build();
    }
    public static  ApiResponse<Void> success(String message){
        return ApiResponse.<Void>builder().message(message).success(true).build();
    }

    public static ResponseEntity<Resource> download(
            Resource resource,
            String fileName,
            String contentType
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(fileName)
                                .build()
                                .toString()
                )
                .body(resource);
    }
}
