package com.merostore.backend.store.service;

import com.merostore.backend.seller.domain.Seller;
import com.merostore.backend.store.domain.Store;
import com.merostore.backend.store.dto.StoreCreateDTO;
import com.merostore.backend.store.dto.StoreDTO;
import com.merostore.backend.store.dto.StoreUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
    StoreCreateDTO createStore(StoreCreateDTO store, Long sellerId);
    Boolean deleteStore(Long id);
    Store findById(Long id);
    StoreUpdateDTO updateStore(StoreUpdateDTO store, Long id);
    String upload(MultipartFile file,Long id);

    StoreDTO getFirstStoreForSeller(Long sellerId);
}
