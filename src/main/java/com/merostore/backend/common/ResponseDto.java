package com.merostore.backend.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseDto<T> {

    private String message;
    private T data;

    protected ResponseDto(T data) {
        this.data = data;
    }

    protected ResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data);
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(message, data);
    }

    public static <T> ResponseDto<T> success(String message) {
        return new ResponseDto<>(message, null);
    }

    public static <T> ResponseDto<T> failure(String message) {
        return new ResponseDto<>(message, null);
    }
}
