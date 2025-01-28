package com.merostore.backend.security.domain.Role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.merostore.backend.common.EnumBase;

import java.util.stream.Stream;

public enum Role implements EnumBase {
    ROLE_SELLER("2"), ROLE_BUYER("3"), ROLE_ADMIN("1");

    private String code;

    Role(String code) {
        this.code=code;
    }

    @JsonCreator
    public static Role decode(final String code) {
        return Stream.of(Role.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}

