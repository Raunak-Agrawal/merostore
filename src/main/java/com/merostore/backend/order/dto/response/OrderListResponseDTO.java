package com.merostore.backend.order.dto.response;

import com.merostore.backend.order.domain.OrderStatus.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderListResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private Long buyerId;
    private Long storeId;
    private List<OrderItemResponseDTO> orderItems;
}
