package com.merostore.backend.product.domain;

import com.merostore.backend.product.domain.Unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUnit {
    private Integer baseQuantity;
    private Unit unit;
}
