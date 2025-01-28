package com.merostore.backend.buyer.service;

import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.buyer.dto.BuyerCreateDTO;

public interface BuyerService {
    BuyerCreateDTO save(Buyer buyer);
}
