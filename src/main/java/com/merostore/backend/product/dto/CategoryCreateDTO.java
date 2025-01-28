package com.merostore.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@JsonIgnoreProperties(value={ "id" }, allowGetters=true)
public class CategoryCreateDTO {
    private Long id;

    @NotBlank(message = "category name is required")
    private String name;

    @NotNull(message = "store id is required")
    private Long storeId;
    private String image;
    private String description;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean isDeleted = false;
}
