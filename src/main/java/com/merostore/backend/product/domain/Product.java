package com.merostore.backend.product.domain;

import com.merostore.backend.store.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Double sellingPrice;

    @Embedded
    private ProductUnit productUnit;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean isDeleted = false;

    private String image;

    @ManyToOne()
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductVariant> variants;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Long getCategory() {
        return category.getId();
    }

    public Long getStore() {
        return store.getId();
    }

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sellingPrice=" + sellingPrice +
                ", productUnit=" + productUnit +
                ", active=" + active +
                ", isDeleted=" + isDeleted +
                ", image='" + image + '\'' +
                ", store=" + store +
                ", variants=" + variants +
                ", category=" + category +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
