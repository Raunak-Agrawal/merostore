package com.merostore.backend.seller.controller;

import com.merostore.backend.exception.ApiValidationException;
import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.service.UserService;
import com.merostore.backend.seller.domain.Seller;
import com.merostore.backend.seller.dto.SellerCreateDTO;
import com.merostore.backend.seller.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String test() {
        return "Seller url invoked";
    }

    @PostMapping("/register")
    public SellerCreateDTO createSeller(@Valid @RequestBody SellerCreateDTO sellerRequest) {

        if (!sellerRequest.getRole().equals(Role.ROLE_SELLER)) {
            throw new ApiValidationException("role id not valid");
        }

        User existingUser = userService.findUserByMobileNumberAndRole(sellerRequest.getMobileNumber(), Role.ROLE_SELLER);
        if (existingUser != null) {
            throw new ApiValidationException("Seller already exists");
        }

        User user = User.builder()
                .mobileNumber(sellerRequest.getMobileNumber())
                .pin(sellerRequest.getPin())
                .role(Role.ROLE_SELLER)
                .build();

        Seller seller = Seller.builder().user(user).build();
        SellerCreateDTO sellerCreateDTO = sellerService.save(seller);
        return sellerCreateDTO;
    }
}