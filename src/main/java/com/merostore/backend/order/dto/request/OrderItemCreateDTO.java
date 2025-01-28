package com.merostore.backend.order.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(value={ "id" }, allowGetters=true)
public class OrderItemCreateDTO {
    private Long id;
    @NotNull(message = "order item quantity is required")
    private Integer quantity;
    @NotNull(message = "order item price is required")
    private Double price;
    @NotNull(message = "product id is required")
    private Long productId;
    private Long[] productVariants;
}
