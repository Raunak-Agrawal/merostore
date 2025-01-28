package com.merostore.backend.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.merostore.backend.exception.ApiValidationException;
import com.merostore.backend.order.repository.OrderRepository;
import com.merostore.backend.store.dto.StoreMetadata.KeyValueMetadata;
import com.merostore.backend.store.dto.StoreMetadata.OrderMetadata;
import com.merostore.backend.store.dto.StoreMetadata.StoreMetadataDTO;
import com.merostore.backend.store.dto.StoreMetadata.TIMEFRAME;
import com.merostore.backend.store.repository.StoreRepository;
import com.merostore.backend.utils.services.GoogleAnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("metadata")
@Slf4j
public class StoreMetadataController {
    @Autowired
    private GoogleAnalyticsService googleAnalyticsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping()
    public StoreMetadataDTO getStoreMetadata(@RequestParam String slug) throws JsonProcessingException {
        String response = GoogleAnalyticsService.getResponse("/" + slug);
        if (response == "") {
            throw new ApiValidationException("No results found");
        }
        JsonNode jsonResponse = objectMapper.readTree(response);
        JsonNode metricsList = jsonResponse.path("data").path("rows").get(0).path("metrics");

        List<KeyValueMetadata> pageviewsList = new ArrayList<>();
        if (metricsList.isArray()) {
            for (int i = 0; i < metricsList.size(); i++) {
                JsonNode metric = metricsList.get(i);
                pageviewsList.add(KeyValueMetadata.builder()
                        .timeframe(TIMEFRAME.values()[i])
                        .value(metric.get("values").get(0).asText())
                        .build());
            }
        }

        Long storeId = storeRepository.findByLinkAndIsDeleted(slug, false).getId();

        OrderMetadata orderDataTillDate = orderRepository.findAllByStoreIdAndCreatedAtBetween(storeId, LocalDateTime.of(LocalDate.of(2021, Month.DECEMBER, 20), LocalTime.MIN), LocalDateTime.now());
        OrderMetadata orderDataToday = orderRepository.findAllByStoreIdAndCreatedAtBetween(storeId, LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.now());

        List<KeyValueMetadata> ordersList = new ArrayList<>();
        ordersList.add(KeyValueMetadata.builder().
                timeframe(TIMEFRAME.TILL_DATE)
                .value(String.valueOf(orderDataTillDate.getTotalOrders()))
                .build());
        ordersList.add(KeyValueMetadata.builder().
                timeframe(TIMEFRAME.TODAY)
                .value(String.valueOf(orderDataToday.getTotalOrders()))
                .build());

        List<KeyValueMetadata> salesList = new ArrayList<>();
        salesList.add(KeyValueMetadata.builder().
                timeframe(TIMEFRAME.TILL_DATE)
                .value(String.valueOf(orderDataTillDate.getTotalPrice()))
                .build());
        salesList.add(KeyValueMetadata.builder().
                timeframe(TIMEFRAME.TODAY)
                .value(String.valueOf(orderDataToday.getTotalPrice()))
                .build());

        return StoreMetadataDTO.builder().
                pageViews(pageviewsList)
                .orders(ordersList)
                .sales(salesList)
                .build();
    }

}
