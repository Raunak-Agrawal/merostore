package com.merostore.backend.product.controller;

import com.merostore.backend.common.MessageResponse;
import com.merostore.backend.common.ResponseDto;
import com.merostore.backend.config.logging.LogExecutionTime;
import com.merostore.backend.product.dto.CategoryCreateDTO;
import com.merostore.backend.product.dto.CategoryListDTO;
import com.merostore.backend.product.dto.CategoryUpdateDTO;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseDto<CategoryCreateDTO> createCategory(@Valid @RequestBody CategoryCreateDTO category) {
        CategoryCreateDTO newCategory = categoryService.createCategory(category);
        return ResponseDto.success("Successfully created category", newCategory);
    }

    @PutMapping("/{id}")
    public ResponseDto<CategoryUpdateDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDTO category) {
        CategoryUpdateDTO updatedCategory = categoryService.updateCategory(category, id);
        return ResponseDto.success("Successfully updated category", updatedCategory);
    }

    @DeleteMapping("/{id}")
    @LogExecutionTime
    public ResponseDto<MessageResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseDto.success("Successfully deleted category");
    }

    @GetMapping("")
    public ResponseDto<List<CategoryListDTO>> getCategoriesByStore(@RequestParam Long storeId) {
        List<CategoryListDTO> categoryList = categoryService.findAllCategoriesByStoreId(storeId);
        return ResponseDto.success("Successfully fetched categories", categoryList);
    }

    @GetMapping("/{id}")
    public ResponseDto<CategoryListDTO> getProductById(@PathVariable Long id) {
        CategoryListDTO category = categoryService.findCategoryById(id);
        return ResponseDto.success("Successfully fetched category", category);
    }

}
