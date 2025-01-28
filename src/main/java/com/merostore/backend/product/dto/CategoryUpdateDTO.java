package com.merostore.backend.product.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonIgnoreProperties(value={ "id" }, allowGetters=true)
public class CategoryUpdateDTO {
    private Long id;

    @NotBlank(message = "category name is required")
    private String name;

    private String description;

    private String image;

    @NotNull(message = "store id is required")
    private Long storeId;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean isDeleted = false;
}


