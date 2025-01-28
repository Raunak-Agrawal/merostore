package com.merostore.backend.store.dto.StoreMetadata;

import com.merostore.backend.order.domain.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StoreMetadataDTO {
    private List<KeyValueMetadata> pageViews;
    private List<KeyValueMetadata> orders;
    private List<KeyValueMetadata> sales;
}


