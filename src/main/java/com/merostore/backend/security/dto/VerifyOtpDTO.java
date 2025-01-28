package com.merostore.backend.security.dto;

import com.merostore.backend.common.ValidateEnum;
import com.merostore.backend.security.domain.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpDTO {
    @NotBlank(message = "mobile number is required")
    private String mobileNumber;

    @ValidateEnum(enumClass = Role.class, message = "role id is not valid")
    private Role role;

    @NotNull(message = "Otp is required")
    private Integer otp;
}
