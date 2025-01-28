package com.merostore.backend.buyer.dto;

import com.merostore.backend.product.dto.CategoryListDTO;
import com.merostore.backend.product.dto.ProductListDTO;
import com.merostore.backend.store.dto.StoreDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BootstrapDTO {
    private StoreDTO storeDetails;
    private List<ProductListDTO> productsDetails;
    private List<CategoryListDTO> categoriesDetails;
}
