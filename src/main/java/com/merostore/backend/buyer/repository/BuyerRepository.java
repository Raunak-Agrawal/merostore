package com.merostore.backend.buyer.repository;

import com.merostore.backend.buyer.domain.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Buyer save(Buyer buyer);
}
