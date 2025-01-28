package com.merostore.backend.product.controller;

import com.merostore.backend.common.MessageResponse;
import com.merostore.backend.common.ResponseDto;
import com.merostore.backend.product.dto.ProductCreateDTO;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.product.dto.ProductUpdateDTO;
import com.merostore.backend.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseDto<ProductCreateDTO> createProduct(@Valid @RequestBody ProductCreateDTO product, Principal principal) {
        log.info("Principle :{}", principal.getName());
        ProductCreateDTO newProduct = productService.createProduct(product);
        return ResponseDto.success("Successfully created product", newProduct);
    }

    @PutMapping(path = "/{id}")
    public ResponseDto<ProductUpdateDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO product) {
        ProductUpdateDTO updatedProduct = productService.updateProduct(product, id);
        return ResponseDto.success("Successfully updated product", updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<MessageResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseDto.success("Successfully deleted product");
    }

    @GetMapping("")
    public ResponseDto<List<ProductListDTO>> getProductsByStore(@RequestParam Long storeId) {
        List<ProductListDTO> productList = productService.findAllProductsByStoreId(storeId);
        return ResponseDto.success("Successfully fetched products", productList);
    }

    @GetMapping("/{id}")
    public ResponseDto<ProductListDTO> getProductById(@PathVariable Long id) {
        ProductListDTO product = productService.findProductById(id);
        return ResponseDto.success("Successfully fetched product", product);
    }
}
