package com.merostore.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.merostore.backend.common.ValidateEnum;
import com.merostore.backend.product.domain.Unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {
    private Long id;
    @NotBlank(message = "product name is required")
    private String name;
    private String description;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean isDeleted = false;

    @NotNull(message = "price value is required")
    private Double price;
    private Double sellingPrice;
    @NotNull(message = "base quantity is required")
    private Integer baseQuantity;
    @ValidateEnum(enumClass = Unit.class, message = "product unit is not valid")
    private Unit unit;

    @NotNull(message = "store id is required")
    private Long storeId;
    @NotNull(message = "category id is required")
    private Long categoryId;
    private String image;
    private List<ProductVariantCreateDTO> variants;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> deleteVariants;
}