package com.merostore.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merostore.backend.common.ValidateEnum;
import com.merostore.backend.product.domain.Variant.VariantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantCreateDTO {

    private Long id;

    @ValidateEnum(enumClass = VariantType.class, message = "variant type is not valid")
    private VariantType type;

    @NotBlank(message = "variant value is required")
    private String value;

    @NotNull(message = "variant price is required")
    private Double price;

    private Double sellingPrice;

    @Builder.Default
    private Boolean isDeleted = false;

    @NotNull(message = "variant product id is required")
    private Long productId;
}
