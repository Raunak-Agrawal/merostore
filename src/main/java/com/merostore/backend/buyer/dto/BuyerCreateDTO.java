package com.merostore.backend.buyer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.merostore.backend.common.ValidateEnum;
import com.merostore.backend.security.domain.Role.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public class BuyerCreateDTO {

    private Long id;

    @NotBlank(message = "mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "pin is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pin;

    @ValidateEnum(enumClass = Role.class, message = "role id is not valid")
    private Role role;
}
