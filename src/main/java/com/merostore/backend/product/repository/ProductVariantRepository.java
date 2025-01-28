package com.merostore.backend.product.repository;

import com.merostore.backend.product.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    ProductVariant findByIdAndIsDeleted(Long id, Boolean isDeleted);
}
