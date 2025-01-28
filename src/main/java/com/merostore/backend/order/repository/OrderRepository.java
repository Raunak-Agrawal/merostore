package com.merostore.backend.order.repository;

import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.order.domain.Order;
import com.merostore.backend.store.dto.StoreMetadata.OrderMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyerOrderByCreatedAtDesc(Buyer buyer);

    List<Order> findAllByStoreIdOrderByCreatedAtDesc(Long storeId);

    @Query("SELECT new com.merostore.backend.store.dto.StoreMetadata.OrderMetadata(SUM(o.totalPrice) AS amount, COUNT(o) AS count) FROM Order o WHERE o.createdAt BETWEEN :start_date AND :end_date AND o.store.id = :storeId")
    OrderMetadata findAllByStoreIdAndCreatedAtBetween(Long storeId, LocalDateTime start_date, LocalDateTime end_date);
}
