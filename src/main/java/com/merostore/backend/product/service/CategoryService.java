package com.merostore.backend.product.service;

import com.merostore.backend.product.domain.Category;
import com.merostore.backend.product.dto.CategoryCreateDTO;
import com.merostore.backend.product.dto.CategoryListDTO;
import com.merostore.backend.product.dto.CategoryUpdateDTO;

import java.util.List;

public interface CategoryService {
    Category save(Category category);

    CategoryCreateDTO createCategory(CategoryCreateDTO category);

    CategoryUpdateDTO updateCategory(CategoryUpdateDTO store, Long id);

    Boolean deleteCategory(Long id);

    List<CategoryListDTO> findAllCategoriesByStoreId(Long storeId);

    CategoryListDTO findCategoryById(Long categoryId);
}
