package com.merostore.backend.buyer.controller;

import com.merostore.backend.buyer.domain.Address;
import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.buyer.dto.AddressCreateDTO;
import com.merostore.backend.buyer.dto.BuyerCreateDTO;
import com.merostore.backend.buyer.service.AddressService;
import com.merostore.backend.buyer.service.BuyerService;
import com.merostore.backend.common.ResponseDto;
import com.merostore.backend.exception.ApiValidationException;
import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("buyer")
public class BuyerController {

    @Autowired
    BuyerService buyerService;

    @Autowired
    AddressService addressService;

    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String test() {
        return "Buyer url invoked";
    }

    @PostMapping("/register")
    public BuyerCreateDTO createBuyer(@Valid @RequestBody BuyerCreateDTO buyerRequest) {

        if (!buyerRequest.getRole().equals(Role.ROLE_BUYER)) {
            throw new ApiValidationException("role id not valid");
        }

        User existingUser = userService.findUserByMobileNumberAndRole(buyerRequest.getMobileNumber(), Role.ROLE_BUYER);
        if (existingUser != null) {
            throw new ApiValidationException("Buyer already exists");
        }

        User user = User.builder()
                .mobileNumber(buyerRequest.getMobileNumber())
                .pin(buyerRequest.getPin())
                .role(Role.ROLE_BUYER)
                .build();

        Buyer buyer = Buyer.builder().user(user).build();
        BuyerCreateDTO buyerCreateDTO = buyerService.save(buyer);
        return buyerCreateDTO;
    }

    @PostMapping("/addresses")
    public AddressCreateDTO createAddressForBuyer(@Valid @RequestBody AddressCreateDTO addressRequest, Authentication authentication) {

        Buyer buyer = userService.findUserByMobileNumberAndRole(authentication.getName(), Role.ROLE_BUYER).getBuyer();

        if (buyer == null) {
            throw new EntityNotFoundException("No buyer found");
        }

        Address address = Address.builder()
                .name(addressRequest.getName())
                .mobileNumber(addressRequest.getMobileNumber())
                .city(addressRequest.getCity())
                .address(addressRequest.getAddress())
                .buyer(buyer)
                .build();

        AddressCreateDTO newAddress = addressService.addAddress(address);
        return newAddress;

    }

    @GetMapping("/addresses")
    public List<AddressCreateDTO> getAllAddressForBuyer(Authentication authentication) {

        Buyer buyer = userService.findUserByMobileNumberAndRole(authentication.getName(), Role.ROLE_BUYER).getBuyer();
        if (buyer == null) {
            throw new EntityNotFoundException("No buyer found");
        }

        return addressService.getAllAddressForBuyer(buyer);

    }

    @PutMapping("/addresses/{id}")
    public AddressCreateDTO updateAddressForBuyer(@PathVariable("id") Long id, @Valid @RequestBody AddressCreateDTO addressUpdateRequest, Authentication authentication) {

        Buyer buyer = userService.findUserByMobileNumberAndRole(authentication.getName(), Role.ROLE_BUYER).getBuyer();
        if (buyer == null) {
            throw new EntityNotFoundException("No buyer found");
        }

        AddressCreateDTO updateAddress = addressService.updateAddress(addressUpdateRequest, id);
        return updateAddress;
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseDto<Object> deleteAddressForBuyer(@PathVariable("id") Long id, Authentication authentication) {

        Buyer buyer = userService.findUserByMobileNumberAndRole(authentication.getName(), Role.ROLE_BUYER).getBuyer();
        if (buyer == null) {
            throw new EntityNotFoundException("No buyer found");
        }

        addressService.deleteAddress(id);
        return ResponseDto.success("Successfully deleted address");
    }
}