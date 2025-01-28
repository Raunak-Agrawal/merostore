package com.merostore.backend.security.dto;

import com.merostore.backend.common.ValidateEnum;
import com.merostore.backend.security.domain.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {

    @NotBlank(message = "mobile number is required")
    private String mobileNumber;
    @ValidateEnum(enumClass = Role.class, message = "role id is not valid")
    private Role role;
}
