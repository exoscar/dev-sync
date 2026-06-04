package org.devsync.spring.common.util;

import org.devsync.spring.common.response.ApiResponse;

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
}
