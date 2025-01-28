package com.merostore.backend.buyer.controller;

import com.merostore.backend.buyer.dto.BootstrapDTO;
import com.merostore.backend.buyer.service.BootstrapService;
import com.merostore.backend.config.logging.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("bootstrap")
public class BootstrapController {

    @Autowired
    private BootstrapService bootstrapService;

    @GetMapping("")
    public BootstrapDTO bootstrapDataForBuyer(@RequestParam String storeLink) {
        log.info("Bootstrap controller called");
        return bootstrapService.getBootstrapDataForBuyer(storeLink);
    }
}
