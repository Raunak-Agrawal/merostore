package com.merostore.backend.buyer.service.impl;

import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.buyer.dto.BuyerCreateDTO;
import com.merostore.backend.buyer.repository.BuyerRepository;
import com.merostore.backend.buyer.service.BuyerService;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.respository.UserRepository;
import com.merostore.backend.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    BuyerRepository buyerRepository;

    @Autowired
    UserService userService;

    @Override
    public BuyerCreateDTO save(Buyer buyer) {

        Buyer newBuyer = buyerRepository.save(buyer);
        User user = userService.save(buyer.getUser());

        BuyerCreateDTO createdBuyer = BuyerCreateDTO.builder()
                .id(newBuyer.getId())
                .mobileNumber(newBuyer.getUser().getMobileNumber())
                .role(newBuyer.getUser().getRole())
                .build();

        return createdBuyer;
    }

}
