package com.merostore.backend.store.repository;

import com.merostore.backend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByLink(String link);
    Store findByIdAndIsDeleted(Long id, Boolean isDeleted);
    Store findFirstBySellerIdAndIsDeleted(Long id, Boolean isDeleted);
    Store findByLinkAndIsDeleted(String storeLink, Boolean isDeleted);
}
