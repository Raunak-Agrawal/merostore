package com.merostore.backend.order.dto.response;

import com.merostore.backend.product.domain.ProductVariant;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.product.dto.ProductVariantCreateDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderItemResponseDTO {
    private Long id;
    private Integer quantity;
    private Double price;
    private Long orderId;
    private ProductListDTO product;
    private List<ProductVariantCreateDTO> orderedProductVariants;
}
