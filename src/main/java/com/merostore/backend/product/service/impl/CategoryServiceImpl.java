package com.merostore.backend.product.service.impl;

import com.merostore.backend.exception.AssetNotFoundException;
import com.merostore.backend.product.domain.Category;
import com.merostore.backend.product.domain.Product;
import com.merostore.backend.product.dto.*;
import com.merostore.backend.product.repository.CategoryRepository;
import com.merostore.backend.product.service.CategoryService;
import com.merostore.backend.store.domain.Store;
import com.merostore.backend.store.repository.StoreRepository;
import com.merostore.backend.utils.services.FileStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    private FileStore fileStore;


    @Override
    public CategoryCreateDTO createCategory(CategoryCreateDTO categoryRequest) {

        Store store = storeRepository.findByIdAndIsDeleted(categoryRequest.getStoreId(), false);
        if (store == null) {
            throw new AssetNotFoundException("Store not found");
        }
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .store(store)
                .description(categoryRequest.getDescription())
                .image(fileStore.getPlaceholderImageURI())
                .build();

        Category createdCategory = categoryRepository.save(category);
        log.info("Creating new category: {}", createdCategory);

        return CategoryCreateDTO.builder()
                .id(createdCategory.getId())
                .name(createdCategory.getName())
                .description(createdCategory.getDescription())
                .active(createdCategory.getActive())
                .isDeleted(createdCategory.getIsDeleted())
                .storeId(createdCategory.getStore())
                .image(createdCategory.getImage())
                .build();

    }

    @Override
    public CategoryUpdateDTO updateCategory(CategoryUpdateDTO categoryRequest, Long id) {
        Category category = categoryRepository.findByIdAndIsDeleted(id, false);
        Store store = storeRepository.findByIdAndIsDeleted(categoryRequest.getStoreId(), false);

        if (category == null) {
            throw new AssetNotFoundException("Category not found");
        }
        if (store == null) {
            throw new AssetNotFoundException("Store not found");
        }
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setActive(categoryRequest.getActive());

        if (!categoryRequest.getImage().isEmpty()) {
            category.setImage(categoryRequest.getImage());
        } else {
            category.setImage(fileStore.getPlaceholderImageURI());
        }

        if (!categoryRequest.getActive().equals(category.getActive())) {
            // category is made inactive, make all products inactive.
            category.getProducts().stream().forEach(product -> {
                product.setActive(categoryRequest.getActive());
            });
        }

        Category updatedCategory = categoryRepository.save(category);
        log.info("Updating category: {}", updatedCategory);

        return CategoryUpdateDTO.builder()
                .id(updatedCategory.getId())
                .name(updatedCategory.getName())
                .description(updatedCategory.getDescription())
                .active(updatedCategory.getActive())
                .isDeleted(updatedCategory.getIsDeleted())
                .storeId(updatedCategory.getStore())
                .image(updatedCategory.getImage())
                .build();
    }

    @Override
    public Category save(Category category) {
        Category newCategory = categoryRepository.save(category);
        return newCategory;
    }

    @Override
    public Boolean deleteCategory(Long id) {
        log.info("Deleting category: {}", id);
        Category category = categoryRepository.findByIdAndIsDeleted(id, false);
        if (category == null) {
            throw new AssetNotFoundException("Category not found");
        }
        category.setIsDeleted(true);
        category.setActive(false);

        // category is deleted, make all products delete.
        category.getProducts().stream().forEach(product -> {
            product.setIsDeleted(true);
            product.setActive(false);
        });

        categoryRepository.save(category);
        return true;

    }

    @Override
    public List<CategoryListDTO> findAllCategoriesByStoreId(Long storeId) {
        List<Category> categoryList = categoryRepository.findAllByStoreIdAndIsDeleted(storeId, false);
        List<CategoryListDTO> categoryListDTO = categoryList.stream().filter(category -> !category.getIsDeleted()).map(category -> CategoryListDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .isDeleted(category.getIsDeleted())
                .storeId(category.getStore())
                .image(category.getImage())
                .noOfProducts(category.getProducts().stream().filter(product -> product.getIsDeleted()!=false).count())
                .build()).collect(Collectors.toList());
        return categoryListDTO;
    }

    @Override
    public CategoryListDTO findCategoryById(Long categoryId) {
        Category category = categoryRepository.findByIdAndIsDeleted(categoryId, false);
        if (category == null) {
            throw new AssetNotFoundException("Category not found");
        }
        return CategoryListDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .isDeleted(category.getIsDeleted())
                .storeId(category.getStore())
                .image(category.getImage())
                .noOfProducts(category.getProducts().stream().filter(product -> product.getIsDeleted()!=false).count())
                .build();
    }
}
