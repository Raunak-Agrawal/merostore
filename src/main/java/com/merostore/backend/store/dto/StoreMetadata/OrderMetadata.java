package com.merostore.backend.store.dto.StoreMetadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMetadata {
    private Double totalPrice;
    private Long totalOrders;
}
