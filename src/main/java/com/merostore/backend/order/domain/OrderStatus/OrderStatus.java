package com.merostore.backend.order.domain.OrderStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.merostore.backend.common.EnumBase;

import java.util.stream.Stream;

public enum OrderStatus implements EnumBase {
    PENDING("pending"),
    COMPLETE("complete"),
    CANCELLED("cancelled"),
    SHIPPED("shipped"),
    IN_PROGRESS("in progress"),
    FAILED("failed");

    private String code;

    OrderStatus(String code) {
        this.code = code;
    }

    @JsonCreator
    public static OrderStatus decode(final String code) {
        return Stream.of(OrderStatus.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
