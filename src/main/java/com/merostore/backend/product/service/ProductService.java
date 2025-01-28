package com.merostore.backend.product.service;

import com.merostore.backend.product.domain.Product;
import com.merostore.backend.product.dto.ProductCreateDTO;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.product.dto.ProductUpdateDTO;

import java.util.List;


public interface ProductService {
    Product save(Product product);
    ProductCreateDTO createProduct(ProductCreateDTO productCreateDTO);

    void deleteProduct(Long id);

    ProductUpdateDTO updateProduct(ProductUpdateDTO product, Long id);

    List<ProductListDTO> findAllProductsByStoreId(Long storeId);

    ProductListDTO findProductById(Long productId);
}
