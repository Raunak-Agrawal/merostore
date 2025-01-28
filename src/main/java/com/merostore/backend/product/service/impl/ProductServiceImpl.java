package com.merostore.backend.product.service.impl;

import com.merostore.backend.exception.AssetNotFoundException;
import com.merostore.backend.product.domain.Category;
import com.merostore.backend.product.domain.Product;
import com.merostore.backend.product.domain.ProductUnit;
import com.merostore.backend.product.domain.ProductVariant;
import com.merostore.backend.product.dto.ProductCreateDTO;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.product.dto.ProductUpdateDTO;
import com.merostore.backend.product.dto.ProductVariantCreateDTO;
import com.merostore.backend.product.repository.CategoryRepository;
import com.merostore.backend.product.repository.ProductRepository;
import com.merostore.backend.product.repository.ProductVariantRepository;
import com.merostore.backend.product.service.ProductService;
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
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductVariantRepository productVariantRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private FileStore fileStore;

    @Override
    public Product save(Product product) {
        Product newProduct = productRepository.save(product);
        return newProduct;
    }

    @Override
    public ProductCreateDTO createProduct(ProductCreateDTO productRequest) {

        Category category = categoryRepository.findByIdAndIsDeleted(productRequest.getCategoryId(), false);
        if (category == null) {
            throw new AssetNotFoundException("Category not found");
        }
        Store store = storeRepository.findByIdAndIsDeleted(productRequest.getStoreId(), false);
        if (store == null) {
            throw new AssetNotFoundException("Store not found");
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .sellingPrice(productRequest.getSellingPrice())
                .productUnit(ProductUnit.builder().baseQuantity(productRequest.getBaseQuantity()).unit(productRequest.getUnit()).build())
                .store(store)
                .category(category)
                .variants(productRequest.getVariants().stream().map(productVariant -> ProductVariant.builder()
                        .id(productVariant.getId())
                        .price(productVariant.getPrice())
                        .sellingPrice(productVariant.getSellingPrice())
                        .type(productVariant.getType())
                        .value(productVariant.getValue())
                        .build()).collect(Collectors.toList()))
                .build();

        if (!productRequest.getImage().isEmpty()) {
            product.setImage(productRequest.getImage());
        } else {
            product.setImage(fileStore.getPlaceholderImageURI());
        }

        Product createdProduct = productRepository.save(product);

        return ProductCreateDTO.builder()
                .id(createdProduct.getId())
                .name(createdProduct.getName())
                .description(createdProduct.getDescription())
                .active(createdProduct.getActive())
                .isDeleted(createdProduct.getIsDeleted())
                .image(createdProduct.getImage())
                .price(createdProduct.getPrice())
                .sellingPrice(createdProduct.getSellingPrice())
                .baseQuantity(createdProduct.getProductUnit().getBaseQuantity())
                .unit(createdProduct.getProductUnit().getUnit())
                .storeId(createdProduct.getStore())
                .categoryId(createdProduct.getCategory())
                .variants(createdProduct.getVariants().stream().map(productVariant -> ProductVariantCreateDTO.builder()
                        .id(productVariant.getId())
                        .price(productVariant.getPrice())
                        .sellingPrice(productVariant.getSellingPrice())
                        .type(productVariant.getType())
                        .value(productVariant.getValue())
                        .productId(createdProduct.getId())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product: {}", id);
        Product product = productRepository.findByIdAndIsDeleted(id, false);
        product.setIsDeleted(true);
        product.setActive(false);
        product.getVariants().stream().forEach(variant -> {
            variant.setIsDeleted(true);
        });
        productRepository.save(product);
    }

    @Override
    public ProductUpdateDTO updateProduct(ProductUpdateDTO productRequest, Long id) {

        Product product = productRepository.findByIdAndIsDeleted(id, false);
        if (product == null) {
            throw new AssetNotFoundException("Product not found");
        }
        Category category = categoryRepository.findByIdAndIsDeleted(productRequest.getCategoryId(), false);
        if (category == null) {
            throw new AssetNotFoundException("Category not found");
        }
        Store store = storeRepository.findByIdAndIsDeleted(productRequest.getStoreId(), false);
        if (store == null) {
            throw new AssetNotFoundException("Store not found");
        }

        // Delete variants if needed.
        if (!productRequest.getDeleteVariants().isEmpty()) {
            product.getVariants().stream().forEach(variant -> {
                if (productRequest.getDeleteVariants().contains(variant.getId())) {
                    variant.setIsDeleted(true);
                }
            });
        }

        productRequest.getVariants().stream().filter(variant -> !productRequest.getDeleteVariants().contains(variant.getId())).forEach(variant -> {
            Boolean shouldAddVariant = false;
            //new variant
            if (variant.getId() == null) {
                shouldAddVariant = true;
            } else {
                //existing variant
                ProductVariant existingVariant = productVariantRepository.findByIdAndIsDeleted(variant.getId(), false);
                if (existingVariant == null) {
                    shouldAddVariant = true;
                }
            }
            if (shouldAddVariant) {
                //update variant.
                product.getVariants().add(ProductVariant.builder()
                        .price(variant.getPrice())
                        .sellingPrice(variant.getSellingPrice())
                        .value(variant.getValue())
                        .type(variant.getType())
                        .product(product)
                        .build());
            }

        });

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setActive(productRequest.getActive());
        product.setPrice(productRequest.getPrice());
        product.setSellingPrice(productRequest.getSellingPrice());
        product.setProductUnit(ProductUnit.builder().baseQuantity(productRequest.getBaseQuantity()).unit(productRequest.getUnit()).build());
        product.setCategory(category);

        if (!productRequest.getImage().isEmpty()) {
            product.setImage(productRequest.getImage());
        } else {
            product.setImage(fileStore.getPlaceholderImageURI());
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Updating product: {}", updatedProduct);

        return ProductUpdateDTO.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .active(updatedProduct.getActive())
                .price(updatedProduct.getPrice())
                .sellingPrice(updatedProduct.getSellingPrice())
                .baseQuantity(updatedProduct.getProductUnit().getBaseQuantity())
                .unit(updatedProduct.getProductUnit().getUnit())
                .storeId(updatedProduct.getStore())
                .categoryId(updatedProduct.getCategory())
                .image(updatedProduct.getImage())
                .variants(updatedProduct.getVariants().stream().filter(productVariant -> !productVariant.getIsDeleted()).map(productVariant -> ProductVariantCreateDTO.builder()
                        .id(productVariant.getId())
                        .price(productVariant.getPrice())
                        .sellingPrice(productVariant.getSellingPrice())
                        .type(productVariant.getType())
                        .value(productVariant.getValue())
                        .productId(updatedProduct.getId())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<ProductListDTO> findAllProductsByStoreId(Long storeId) {
        List<Product> productList = productRepository.findAllByStoreIdAndIsDeleted(storeId, false);
        List<ProductListDTO> productListDTO = productList.stream().filter(product -> !product.getIsDeleted()).map(product -> ProductListDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .active(product.getActive())
                .isDeleted(product.getIsDeleted())
                .price(product.getPrice())
                .sellingPrice(product.getSellingPrice())
                .baseQuantity(product.getProductUnit().getBaseQuantity())
                .unit(product.getProductUnit().getUnit())
                .storeId(product.getStore())
                .categoryId(product.getCategory())
                .image(product.getImage())
                .variants(product.getVariants().stream().filter(variant -> !variant.getIsDeleted()).map(productVariant -> ProductVariantCreateDTO.builder()
                        .id(productVariant.getId())
                        .price(productVariant.getPrice())
                        .sellingPrice(productVariant.getSellingPrice())
                        .type(productVariant.getType())
                        .value(productVariant.getValue())
                        .isDeleted(productVariant.getIsDeleted())
                        .productId(product.getId())
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

        return productListDTO;
    }

    @Override
    public ProductListDTO findProductById(Long productId) {
        Product product = productRepository.findByIdAndIsDeleted(productId, false);
        if (product == null) {
            throw new AssetNotFoundException("Product not found");
        }
        return ProductListDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .active(product.getActive())
                .isDeleted(product.getIsDeleted())
                .price(product.getPrice())
                .sellingPrice(product.getSellingPrice())
                .baseQuantity(product.getProductUnit().getBaseQuantity())
                .unit(product.getProductUnit().getUnit())
                .storeId(product.getStore())
                .categoryId(product.getCategory())
                .image(product.getImage())
                .variants(product.getVariants().stream().filter(variant -> !variant.getIsDeleted()).map(productVariant -> ProductVariantCreateDTO.builder()
                        .id(productVariant.getId())
                        .price(productVariant.getPrice())
                        .sellingPrice(productVariant.getSellingPrice())
                        .type(productVariant.getType())
                        .value(productVariant.getValue())
                        .isDeleted(productVariant.getIsDeleted())
                        .productId(product.getId())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
