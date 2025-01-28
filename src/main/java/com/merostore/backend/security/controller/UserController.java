package com.merostore.backend.security.controller;

import com.merostore.backend.common.ResponseDto;
import com.merostore.backend.exception.ApiValidationException;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.dto.ForgotPasswordDTO;
import com.merostore.backend.security.dto.PasswordUpdateDTO;
import com.merostore.backend.security.dto.VerifyOtpDTO;
import com.merostore.backend.security.service.UserService;
import com.merostore.backend.utils.services.EmailService;
import com.merostore.backend.utils.services.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/forgot-password")
    public ResponseDto<Object> resetPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        User existingUser = userService.findUserByMobileNumberAndRole(forgotPasswordDTO.getMobileNumber(), forgotPasswordDTO.getRole());
        if (existingUser == null) {
            throw new ApiValidationException("User doesn't exist");
        }
        String keyToCache = "OTP:" + existingUser.getMobileNumber() + ":" + forgotPasswordDTO.getRole().getCode();
        otpService.sendOTP(existingUser.getMobileNumber(), keyToCache);
        return ResponseDto.success("Password reset otp sent successfully");
    }

    @PostMapping(value = "/verify-otp")
    public ResponseDto<Object> verifyOTP(@Valid @RequestBody VerifyOtpDTO verifyOtpDTO) {
        User existingUser = userService.findUserByMobileNumberAndRole(verifyOtpDTO.getMobileNumber(), verifyOtpDTO.getRole());
        if (existingUser == null) {
            throw new ApiValidationException("User doesn't exist");
        }

        String keyCached = "OTP:" + existingUser.getMobileNumber() + ":" + verifyOtpDTO.getRole().getCode();

        Boolean isValid = otpService.validateOTP(verifyOtpDTO.getOtp(), keyCached);
        if (!isValid) {
            throw new ApiValidationException("Incorrect OTP. Please try again");
        }
        return ResponseDto.success("OTP verified successfully");
    }

    @PostMapping(value = "/update-password")
    public ResponseDto<Object> updatePassword(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User existingUser = userService.findUserByMobileNumberAndRole(passwordUpdateDTO.getMobileNumber(), passwordUpdateDTO.getRole());
        if (existingUser == null) {
            throw new ApiValidationException("User doesn't exist");
        }

        String keyCached = "OTP:" + existingUser.getMobileNumber() + ":" + passwordUpdateDTO.getRole().getCode();

        Boolean isValid = otpService.validateOTP(passwordUpdateDTO.getOtp(), keyCached);
        if (!isValid) {
            throw new ApiValidationException("Incorrect OTP. Please try again");
        }
        userService.changeUserPassword(existingUser, passwordUpdateDTO.getPin());
        otpService.clearOTP(keyCached);
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return ResponseDto.success("Pin reset successfully. Please login again");
    }
}
