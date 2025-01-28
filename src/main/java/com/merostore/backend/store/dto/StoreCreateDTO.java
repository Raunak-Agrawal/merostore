package com.merostore.backend.store.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merostore.backend.common.ValidateEnum;
import com.merostore.backend.store.domain.BusinessCategory.BusinessCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreCreateDTO {

    private Long id;

    @NotBlank(message = "store name is required")
    private String name;

    @ValidateEnum(enumClass = BusinessCategory.class, message = "business category is not valid")
    private BusinessCategory businessCategory;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "address is required")
    private String address;

    private String link;
    private String image;

    private Long sellerId;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean isDeleted = false;
}
