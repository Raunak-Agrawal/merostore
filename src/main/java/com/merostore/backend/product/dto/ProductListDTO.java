package com.merostore.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merostore.backend.product.domain.Unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private Boolean isDeleted;
    private Double price;
    private Double sellingPrice;
    private Integer baseQuantity;
    private Unit unit;
    private Long storeId;
    private Long categoryId;
    private String image;
    private List<ProductVariantCreateDTO> variants;
}
