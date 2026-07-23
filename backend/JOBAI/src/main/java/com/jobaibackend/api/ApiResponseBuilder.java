package com.jobaibackend.api;

import java.time.LocalDateTime;

/**
 * Factory class for creating standardized API responses.
 */
public final class ApiResponseBuilder {

    private ApiResponseBuilder() {
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(
                false,
                message,
                null,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> failure(String message, T data) {
        return new ApiResponse<>(
                false,
                message,
                data,
                LocalDateTime.now()
        );
    }
}