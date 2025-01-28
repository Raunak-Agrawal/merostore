package com.merostore.backend.store.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merostore.backend.store.domain.BusinessCategory.BusinessCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private Long id;
    private String name;
    private String image;
    private String link;
    private BusinessCategory businessCategory;
    private String city;
    private String address;
    private Boolean isDeleted;
    private Boolean active;
    private Long sellerId;
}
