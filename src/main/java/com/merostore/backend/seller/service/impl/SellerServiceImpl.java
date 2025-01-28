package com.merostore.backend.seller.service.impl;

import com.merostore.backend.exception.AssetNotFoundException;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.respository.UserRepository;
import com.merostore.backend.security.service.UserService;
import com.merostore.backend.seller.domain.Seller;
import com.merostore.backend.seller.dto.SellerCreateDTO;
import com.merostore.backend.seller.repository.SellerRepository;
import com.merostore.backend.seller.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

    @Override
    public SellerCreateDTO save(Seller seller) {

        Seller newSeller = sellerRepository.save(seller);

        User user = userService.save(seller.getUser());

        SellerCreateDTO createdSeller = SellerCreateDTO.builder()
                .id(newSeller.getId())
                .mobileNumber(newSeller.getUser().getMobileNumber())
                .role(newSeller.getUser().getRole())
                .build();

        return createdSeller;
    }

    @Override
    public Seller findById(Long id) {
        Seller seller = sellerRepository.findById(id).get();
        if (seller == null) {
            throw new AssetNotFoundException("Seller not found");
        }
        return seller;
    }

}
