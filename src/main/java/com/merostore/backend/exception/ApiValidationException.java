package com.merostore.backend.exception;

import lombok.Getter;

@Getter
public class ApiValidationException extends RuntimeException {

    private final Throwable error;
    private final String message;
    private final String code;

    public ApiValidationException() {
        this("", "", null);
    }

    public ApiValidationException(String message) {
        this(message, "", null);
    }

    public ApiValidationException(String message, String code, Throwable error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }
}
