package com.merostore.backend.buyer.service;

import com.merostore.backend.buyer.dto.BootstrapDTO;

public interface BootstrapService {
    BootstrapDTO getBootstrapDataForBuyer(String storeLink);
}
