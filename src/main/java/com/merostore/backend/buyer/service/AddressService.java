package com.merostore.backend.buyer.service;

import com.merostore.backend.buyer.domain.Address;
import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.buyer.dto.AddressCreateDTO;

import java.util.List;

public interface AddressService {
    AddressCreateDTO addAddress(Address address);

    List<AddressCreateDTO> getAllAddressForBuyer(Buyer buyer);

    AddressCreateDTO updateAddress(AddressCreateDTO addressUpdateRequest, Long id);

    Boolean deleteAddress(Long id);
}
