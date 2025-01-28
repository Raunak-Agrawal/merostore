package com.merostore.backend.product.repository;

import com.merostore.backend.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByIdAndIsDeleted(Long id, Boolean isDeleted);

    List<Category> findAllByStoreIdAndIsDeleted(Long storeId, Boolean isDeleted);
}
