package com.merostore.backend.order.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(value={ "id" }, allowGetters=true)
public class OrderCreateDTO {
    private Long id;
    @NotNull(message = "order total is required")
    private Double totalPrice;
    @NotNull(message = "store id is required")
    private Long storeId;
    private List<OrderItemCreateDTO> orderItems;
}
