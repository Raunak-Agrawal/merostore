package com.merostore.backend.exception;

public class UnauthorizedException extends RuntimeException {
    private final Throwable error;
    private final String message;
    private final String code;

    public UnauthorizedException() {
        this("", "", (Throwable)null);
    }

    public UnauthorizedException(String message) {
        this(message, "", (Throwable)null);
    }

    public UnauthorizedException(String message, String code, Throwable error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public Throwable getError() {
        return this.error;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }
}
