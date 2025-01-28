package com.merostore.backend.order.dto.request;

import com.merostore.backend.common.ValidateEnum;
import com.merostore.backend.order.domain.OrderStatus.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDTO {
    @ValidateEnum(enumClass = OrderStatus.class, message = "order status is not valid")
    private OrderStatus orderStatus;
}
