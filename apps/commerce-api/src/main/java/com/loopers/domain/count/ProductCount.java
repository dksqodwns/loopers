package com.loopers.domain.count;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCount extends BaseEntity {

    @Column(name = "ref_product_id")
    private Long productId;

    @AttributeOverride(name = "count", column = @Column(name = "like_count"))
    @Embedded
    private Count likeCount;


    @Getter
    @EqualsAndHashCode
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Count {

        private Long count;

    }
}


