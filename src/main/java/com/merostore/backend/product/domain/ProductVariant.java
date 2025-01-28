package com.merostore.backend.product.domain;

import com.merostore.backend.product.domain.Variant.VariantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "productvariants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private VariantType type;

    private String value;
    private Double price;
    private Double sellingPrice;

    @Builder.Default
    private Boolean isDeleted = false;

    @ManyToOne()
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    public Long getProduct() {
        return product.getId();
    }
}

