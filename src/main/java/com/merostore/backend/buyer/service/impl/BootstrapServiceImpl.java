package com.merostore.backend.buyer.service.impl;

import com.merostore.backend.buyer.dto.BootstrapDTO;
import com.merostore.backend.buyer.service.BootstrapService;
import com.merostore.backend.exception.AssetNotFoundException;
import com.merostore.backend.product.dto.CategoryListDTO;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.product.dto.ProductVariantCreateDTO;
import com.merostore.backend.store.domain.Store;
import com.merostore.backend.store.dto.StoreDTO;
import com.merostore.backend.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BootstrapServiceImpl implements BootstrapService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public BootstrapDTO getBootstrapDataForBuyer(String storeLink) {
        Store store = storeRepository.findByLinkAndIsDeleted(storeLink, false);
        if (store == null) {
        }
        return BootstrapDTO.builder()
                .storeDetails(StoreDTO.builder()
                        .id(store.getId())
                        .name(store.getName())
                        .image(store.getImage())
                        .link(store.getLink())
                        .businessCategory(store.getBusinessCategory())
                        .city(store.getCity())
                        .address(store.getAddress())
                        .isDeleted(store.getIsDeleted())
                        .active(store.getActive())
                        .sellerId(store.getSeller())
                        .build())
                .productsDetails(store.getProducts().stream().filter(product -> !product.getIsDeleted()).map(product -> ProductListDTO.builder()
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
                        .build()).collect(Collectors.toList()))
                .categoriesDetails(store.getCategories().stream().filter(category -> !category.getIsDeleted()).map(category -> CategoryListDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .active(category.getActive())
                        .isDeleted(category.getIsDeleted())
                        .storeId(category.getStore())
                        .image(category.getImage())
                        .noOfProducts(category.getProducts().stream().filter(product -> !product.getIsDeleted()).count())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
