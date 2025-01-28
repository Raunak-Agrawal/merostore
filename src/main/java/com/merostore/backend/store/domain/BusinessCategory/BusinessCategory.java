package com.merostore.backend.store.domain.BusinessCategory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.merostore.backend.common.EnumBase;

import java.util.stream.Stream;

public enum BusinessCategory implements EnumBase {
    KIRANA_STORE("1"),
    FASHION_CLOTHING("2"),
    HARDWARE_CONSTRUCTION("3"),
    MEDICAL_PHARMACY("4");

    private String code;

    BusinessCategory(String code) {
        this.code = code;
    }

    @JsonCreator
    public static BusinessCategory decode(final String code) {
        return Stream.of(BusinessCategory.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }


    @JsonValue
    public String getCode() {
        return code;

    }

//    public static boolean isValid(BusinessCategory businessCategory) {
//        if(businessCategory==null){
//            return false;
//        }
//        for (BusinessCategory item : BusinessCategory.values()) {
//            if (item.getCode().equals(businessCategory)) {
//                return true;
//            }
//        }
//        return false;
//    }
}

