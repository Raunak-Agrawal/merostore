package com.merostore.backend.product.domain.Variant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.merostore.backend.common.EnumBase;

import java.util.stream.Stream;

public enum VariantType implements EnumBase {
    SIZE("size"),
    COLOR("color");

    private String code;

    private VariantType(String code) {
        this.code=code;
    }

    @JsonCreator
    public static VariantType decode(final String code) {
        return Stream.of(VariantType.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
