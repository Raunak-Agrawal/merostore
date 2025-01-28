package com.merostore.backend.order.service;

import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.common.ResponseDto;
import com.merostore.backend.order.dto.request.OrderCreateDTO;
import com.merostore.backend.order.dto.request.OrderUpdateDTO;
import com.merostore.backend.order.dto.response.OrderListResponseDTO;

import java.util.List;

public interface OrderService {
    void placeOrder(OrderCreateDTO orderCreateDTO, Buyer buyer);

    List<OrderListResponseDTO> listOrdersForBuyer(Buyer buyer);

    List<OrderListResponseDTO> listOrdersForStore(Long storeId);

    Boolean updateOrder(OrderUpdateDTO orderUpdateDTO, Long id);

    OrderListResponseDTO findOrderById(Long id);
}
