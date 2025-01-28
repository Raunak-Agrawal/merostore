package com.merostore.backend.buyer.repository;

import com.merostore.backend.buyer.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Long> {
    Address save(Address address);

    List<Address> findAllByBuyerId(Long id);
}
