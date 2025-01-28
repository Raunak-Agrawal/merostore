package com.merostore.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private Boolean isDeleted;
    private Long storeId;
    private String image;
    private Long noOfProducts;
}
