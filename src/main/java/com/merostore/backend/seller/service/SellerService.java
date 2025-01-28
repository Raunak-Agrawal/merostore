package com.merostore.backend.seller.service;

import com.merostore.backend.seller.domain.Seller;
import com.merostore.backend.seller.dto.SellerCreateDTO;

public interface SellerService {
    SellerCreateDTO save(Seller seller);

    Seller findById(Long id);
}
