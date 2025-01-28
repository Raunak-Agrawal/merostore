package com.merostore.backend.exception;

import lombok.Getter;

@Getter
public class AssetNotFoundException extends RuntimeException {
    private final Throwable error;
    private final String message;
    private final String code;

    public AssetNotFoundException() {
        this("", "", null);
    }

    public AssetNotFoundException(String message) {
        this(message, "", null);
    }

    public AssetNotFoundException(String message, String code, Throwable error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }
}
