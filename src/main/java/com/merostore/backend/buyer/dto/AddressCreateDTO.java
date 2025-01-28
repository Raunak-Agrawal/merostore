package com.merostore.backend.buyer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@JsonIgnoreProperties(value={ "id", "buyerId" }, allowGetters=true)
public class AddressCreateDTO {

    private Long id;
    private Long buyerId;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "address is required")
    private String address;
}