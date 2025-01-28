package com.merostore.backend.order.controller;

import com.merostore.backend.common.ResponseDto;
import com.merostore.backend.order.domain.Order;
import com.merostore.backend.order.dto.request.OrderCreateDTO;
import com.merostore.backend.order.dto.request.OrderUpdateDTO;
import com.merostore.backend.order.dto.response.OrderListResponseDTO;
import com.merostore.backend.order.service.OrderService;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    // place order
    @PostMapping
    public ResponseDto<Object> placeOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO, Principal principal) {
        User user = userService.findUserByMobileNumberAndRole(principal.getName(), Role.ROLE_BUYER);
        // place the order
        orderService.placeOrder(orderCreateDTO, user.getBuyer());
        return ResponseDto.success("Order has been placed");
    }

    @GetMapping
    public ResponseDto<List<OrderListResponseDTO>> getAllOrders(@RequestParam(required = false) String storeId, Principal principal) {
        List<OrderListResponseDTO> orderList = null;
        if (storeId.isEmpty()) {
            User user = userService.findUserByMobileNumberAndRole(principal.getName(), Role.ROLE_BUYER);
            // get orders for buyer
            orderList = orderService.listOrdersForBuyer(user.getBuyer());
        } else {
            // get orders for seller
            orderList = orderService.listOrdersForStore(Long.valueOf(storeId));
        }
        return ResponseDto.success("Successfully retrieved orders", orderList);
    }

    // update order
    @PutMapping("/{id}")
    public ResponseDto<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderUpdateDTO orderUpdateDTO) {
        orderService.updateOrder(orderUpdateDTO, id);
        return ResponseDto.success("Successfully updated order");
    }

    @GetMapping("/{id}")
    public ResponseDto<OrderListResponseDTO> getOrderById(@PathVariable Long id) {
        OrderListResponseDTO order = orderService.findOrderById(id);
        return ResponseDto.success("Successfully fetched order", order);
    }
}