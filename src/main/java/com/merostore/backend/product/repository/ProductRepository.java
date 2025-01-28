package com.merostore.backend.product.repository;

import com.merostore.backend.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByStoreIdAndIsDeleted(Long storeId, Boolean isDeleted);
    Product findByIdAndIsDeleted(Long id, Boolean isDeleted);
}
