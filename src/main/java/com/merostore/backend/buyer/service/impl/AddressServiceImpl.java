package com.merostore.backend.buyer.service.impl;

import com.merostore.backend.buyer.domain.Address;
import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.buyer.dto.AddressCreateDTO;
import com.merostore.backend.buyer.repository.AddressRepository;
import com.merostore.backend.buyer.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public AddressCreateDTO addAddress(Address addressRequest) {

        Address newAddress = addressRepository.save(addressRequest);

        return AddressCreateDTO.builder()
                .id(newAddress.getId())
                .name(addressRequest.getName())
                .mobileNumber(addressRequest.getMobileNumber())
                .city(addressRequest.getCity())
                .address(addressRequest.getAddress())
                .buyerId(addressRequest.getBuyer().getId())
                .build();
    }

    public List<AddressCreateDTO> getAllAddressForBuyer(Buyer buyer) {
        return addressRepository.findAllByBuyerId(buyer.getId()).stream().map(iAddress -> AddressCreateDTO.builder()
                .id(iAddress.getId())
                .mobileNumber(iAddress.getMobileNumber())
                .name(iAddress.getName())
                .address(iAddress.getAddress())
                .city(iAddress.getCity())
                .buyerId(buyer.getId())
                .build()).collect(Collectors.toList());
    }

    @Override
    public AddressCreateDTO updateAddress(AddressCreateDTO addressUpdateRequest, Long id) {
        Address existingAddress = addressRepository.findById(id).get();
        existingAddress.setCity(addressUpdateRequest.getCity());
        existingAddress.setAddress(addressUpdateRequest.getAddress());
        existingAddress.setMobileNumber(addressUpdateRequest.getMobileNumber());
        existingAddress.setName(addressUpdateRequest.getName());

        Address address = addressRepository.save(existingAddress); //Save updated address

        return AddressCreateDTO.builder()
                .id(address.getId())
                .name(address.getName())
                .mobileNumber(address.getMobileNumber())
                .city(address.getCity())
                .address(address.getAddress())
                .buyerId(address.getBuyer().getId())
                .build();
    }

    @Override
    public Boolean deleteAddress(Long id) {
        log.info("Successfully deleted address");
        addressRepository.deleteById(id);
        return true;
    }

}
