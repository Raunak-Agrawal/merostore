package com.merostore.backend.order.service.impl;

import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.exception.AssetNotFoundException;
import com.merostore.backend.order.domain.Order;
import com.merostore.backend.order.domain.OrderItem;
import com.merostore.backend.order.domain.OrderStatus.OrderStatus;
import com.merostore.backend.order.dto.request.OrderCreateDTO;
import com.merostore.backend.order.dto.request.OrderItemCreateDTO;
import com.merostore.backend.order.dto.request.OrderUpdateDTO;
import com.merostore.backend.order.dto.response.OrderItemResponseDTO;
import com.merostore.backend.order.dto.response.OrderListResponseDTO;
import com.merostore.backend.order.repository.OrderItemRepository;
import com.merostore.backend.order.repository.OrderRepository;
import com.merostore.backend.order.service.OrderService;
import com.merostore.backend.product.domain.Product;
import com.merostore.backend.product.domain.ProductVariant;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.product.dto.ProductVariantCreateDTO;
import com.merostore.backend.product.repository.ProductRepository;
import com.merostore.backend.product.repository.ProductVariantRepository;
import com.merostore.backend.store.domain.Store;
import com.merostore.backend.store.repository.StoreRepository;
import com.merostore.backend.utils.services.FileStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private FileStore fileStore;

    public void placeOrder(OrderCreateDTO orderCreateDTO, Buyer buyer) {

        Store store = storeRepository.findByIdAndIsDeleted(orderCreateDTO.getStoreId(), false);

        if (store == null) {
            throw new AssetNotFoundException("Store not found");
        }

        List<OrderItemCreateDTO> orderItemList = orderCreateDTO.getOrderItems();

        // create the order and save it
        Order order = Order.builder()
                .buyer(buyer)
                .store(store)
                .totalPrice(orderCreateDTO.getTotalPrice())
                .orderStatus(OrderStatus.PENDING)
                .build();

        Order createdOrder = orderRepository.save(order);

        for (OrderItemCreateDTO orderItem : orderItemList) {
            // create orderItem and save each one
            Product product = productRepository.findByIdAndIsDeleted(orderItem.getProductId(), false);
            if (product == null) {
                throw new AssetNotFoundException("Product not found");
            }
            for (Long vi : orderItem.getProductVariants()) {
                if (vi != null) {
                    ProductVariant productVariant = productVariantRepository.findByIdAndIsDeleted(vi, false);
                    if (productVariant == null) {
                        throw new AssetNotFoundException("Product variant not found");
                    }
                }
            }

            OrderItem item = OrderItem.builder()
                    .price(orderItem.getPrice())
                    .quantity(orderItem.getQuantity())
                    .order(createdOrder)
                    .product(product)
                    .productVariants(orderItem.getProductVariants())
                    .build();
            // add to order item list
            orderItemRepository.save(item);
        }

        log.info("Order created successfully");
    }

    public List<OrderListResponseDTO> listOrdersForBuyer(Buyer buyer) {
        List<Order> orderList = orderRepository.findAllByBuyerOrderByCreatedAtDesc(buyer);
        List<OrderListResponseDTO> orderListResponseDTOList = orderList.stream().map(oi -> OrderListResponseDTO.builder()
                .buyerId(oi.getBuyer())
                .id(oi.getId())
                .createdAt(oi.getCreatedAt())
                .updatedAt(oi.getUpdatedAt())
                .totalPrice(oi.getTotalPrice())
                .orderStatus(oi.getOrderStatus())
                .storeId(oi.getStore().getId())
                .orderItems(oi.getOrderItems().stream().map(oli -> OrderItemResponseDTO.builder()
                        .id(oli.getId())
                        .quantity(oli.getQuantity())
                        .price(oli.getPrice())
                        .orderId(oli.getOrder())
                        .product(ProductListDTO.builder()
                                .id(oli.getProduct().getId())
                                .name(oli.getProduct().getName())
                                .description(oli.getProduct().getDescription())
                                .active(oli.getProduct().getActive())
                                .isDeleted(oli.getProduct().getIsDeleted())
                                .price(oli.getProduct().getPrice())
                                .sellingPrice(oli.getProduct().getSellingPrice())
                                .baseQuantity(oli.getProduct().getProductUnit().getBaseQuantity())
                                .unit(oli.getProduct().getProductUnit().getUnit())
                                .storeId(oli.getProduct().getStore())
                                .categoryId(oli.getProduct().getCategory())
                                .image(fileStore.getObjectURI(oi.getStore().getId(), oli.getProduct().getImage()))
                                .variants(oli.getProduct().getVariants().stream().map(productVariant -> ProductVariantCreateDTO.builder()
                                        .id(productVariant.getId())
                                        .price(productVariant.getPrice())
                                        .sellingPrice(productVariant.getSellingPrice())
                                        .type(productVariant.getType())
                                        .value(productVariant.getValue())
                                        .isDeleted(productVariant.getIsDeleted())
                                        .productId(productVariant.getProduct())
                                        .build()).collect(Collectors.toList()))
                                .build())
                        .orderedProductVariants(Arrays.stream(oli.getProductVariants()).map(vi -> {
                            ProductVariant productVariant = productVariantRepository.findById(vi).get();
                            if (productVariant == null) {
                                throw new AssetNotFoundException("Product variant not found");
                            }
                            return ProductVariantCreateDTO.builder()
                                    .id(productVariant.getId())
                                    .price(productVariant.getPrice())
                                    .sellingPrice(productVariant.getSellingPrice())
                                    .type(productVariant.getType())
                                    .value(productVariant.getValue())
                                    .isDeleted(productVariant.getIsDeleted())
                                    .productId(productVariant.getProduct())
                                    .build();
                        }).collect(Collectors.toList()))
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

        return orderListResponseDTOList;
    }

    public List<OrderListResponseDTO> listOrdersForStore(Long storeId) {
        List<Order> orderList = orderRepository.findAllByStoreIdOrderByCreatedAtDesc(storeId);
        List<OrderListResponseDTO> orderListResponseDTOList = orderList.stream().map(oi -> OrderListResponseDTO.builder()
                .buyerId(oi.getBuyer())
                .id(oi.getId())
                .createdAt(oi.getCreatedAt())
                .updatedAt(oi.getUpdatedAt())
                .totalPrice(oi.getTotalPrice())
                .orderStatus(oi.getOrderStatus())
                .storeId(oi.getStore().getId())
                .orderItems(oi.getOrderItems().stream().map(oli -> OrderItemResponseDTO.builder()
                        .id(oli.getId())
                        .quantity(oli.getQuantity())
                        .price(oli.getPrice())
                        .orderId(oli.getOrder())
                        .product(ProductListDTO.builder()
                                .id(oli.getProduct().getId())
                                .name(oli.getProduct().getName())
                                .description(oli.getProduct().getDescription())
                                .active(oli.getProduct().getActive())
                                .isDeleted(oli.getProduct().getIsDeleted())
                                .price(oli.getProduct().getPrice())
                                .sellingPrice(oli.getProduct().getSellingPrice())
                                .baseQuantity(oli.getProduct().getProductUnit().getBaseQuantity())
                                .unit(oli.getProduct().getProductUnit().getUnit())
                                .storeId(oli.getProduct().getStore())
                                .categoryId(oli.getProduct().getCategory())
                                .image(fileStore.getObjectURI(oi.getStore().getId(), oli.getProduct().getImage()))
                                .variants(oli.getProduct().getVariants().stream().map(productVariant -> ProductVariantCreateDTO.builder()
                                        .id(productVariant.getId())
                                        .price(productVariant.getPrice())
                                        .sellingPrice(productVariant.getSellingPrice())
                                        .type(productVariant.getType())
                                        .value(productVariant.getValue())
                                        .isDeleted(productVariant.getIsDeleted())
                                        .productId(productVariant.getProduct())
                                        .build()).collect(Collectors.toList()))
                                .build())
                        .orderedProductVariants(Arrays.stream(oli.getProductVariants()).map(vi -> {
                            ProductVariant productVariant = productVariantRepository.findById(vi).get();
                            if (productVariant == null) {
                                throw new AssetNotFoundException("Product variant not found");
                            }
                            return ProductVariantCreateDTO.builder()
                                    .id(productVariant.getId())
                                    .price(productVariant.getPrice())
                                    .sellingPrice(productVariant.getSellingPrice())
                                    .type(productVariant.getType())
                                    .value(productVariant.getValue())
                                    .isDeleted(productVariant.getIsDeleted())
                                    .productId(productVariant.getProduct())
                                    .build();
                        }).collect(Collectors.toList()))
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

        return orderListResponseDTOList;
    }

    @Override
    public Boolean updateOrder(OrderUpdateDTO orderUpdateDTO, Long id) {
        Optional<Order> Optionalorder = orderRepository.findById(id);
        if (Optionalorder.isPresent()) {
            Order order = Optionalorder.get();
            order.setOrderStatus(orderUpdateDTO.getOrderStatus());
            orderRepository.save(order);
            return true;
        } else {
            throw new AssetNotFoundException("Order not found");
        }
    }

    @Override
    public OrderListResponseDTO findOrderById(Long id) {
        Order order = orderRepository.findById(id).get();
        if (order == null) {
            throw new AssetNotFoundException("Order not found");
        }
        return OrderListResponseDTO.builder()
                .buyerId(order.getBuyer())
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .storeId(order.getStore().getId())
                .orderItems(order.getOrderItems().stream().map(oli -> OrderItemResponseDTO.builder()
                        .id(oli.getId())
                        .quantity(oli.getQuantity())
                        .price(oli.getPrice())
                        .orderId(oli.getOrder())
                        .product(ProductListDTO.builder()
                                .id(oli.getProduct().getId())
                                .name(oli.getProduct().getName())
                                .description(oli.getProduct().getDescription())
                                .active(oli.getProduct().getActive())
                                .isDeleted(oli.getProduct().getIsDeleted())
                                .price(oli.getProduct().getPrice())
                                .sellingPrice(oli.getProduct().getSellingPrice())
                                .baseQuantity(oli.getProduct().getProductUnit().getBaseQuantity())
                                .unit(oli.getProduct().getProductUnit().getUnit())
                                .storeId(oli.getProduct().getStore())
                                .categoryId(oli.getProduct().getCategory())
                                .image(fileStore.getObjectURI(order.getStore().getId(), oli.getProduct().getImage()))
                                .variants(oli.getProduct().getVariants().stream().map(productVariant -> ProductVariantCreateDTO.builder()
                                        .id(productVariant.getId())
                                        .price(productVariant.getPrice())
                                        .sellingPrice(productVariant.getSellingPrice())
                                        .type(productVariant.getType())
                                        .value(productVariant.getValue())
                                        .isDeleted(productVariant.getIsDeleted())
                                        .productId(productVariant.getProduct())
                                        .build()).collect(Collectors.toList()))
                                .build())
                        .orderedProductVariants(Arrays.stream(oli.getProductVariants()).map(vi -> {
                            ProductVariant productVariant = productVariantRepository.findById(vi).get();
                            if (productVariant == null) {
                                throw new AssetNotFoundException("Product variant not found");
                            }
                            return ProductVariantCreateDTO.builder()
                                    .id(productVariant.getId())
                                    .price(productVariant.getPrice())
                                    .sellingPrice(productVariant.getSellingPrice())
                                    .type(productVariant.getType())
                                    .value(productVariant.getValue())
                                    .isDeleted(productVariant.getIsDeleted())
                                    .productId(productVariant.getProduct())
                                    .build();
                        }).collect(Collectors.toList()))
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
