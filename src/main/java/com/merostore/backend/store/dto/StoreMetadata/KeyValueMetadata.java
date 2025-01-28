package com.merostore.backend.store.dto.StoreMetadata;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class KeyValueMetadata {
    private String value;
    private TIMEFRAME timeframe;
}
