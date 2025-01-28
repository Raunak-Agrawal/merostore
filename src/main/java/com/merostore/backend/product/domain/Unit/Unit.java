package com.merostore.backend.product.domain.Unit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.merostore.backend.common.EnumBase;

import java.util.stream.Stream;

public enum Unit implements EnumBase {
    PIECE("piece"),
    KG("Kg"),
    GRAM("gm"),
    PACKET("packet"),
    DOZEN("dozen"),
    BOX("box"),
    LITER("liter");

    private String code;

    Unit(String code) {
        this.code = code;
    }

    @JsonCreator
    public static Unit decode(final String code) {
        return Stream.of(Unit.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
