package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Embedded
    private BrandId brandId;

    private String name;
    
    private Long price;

    private Long likeCount = 0L;

    public Product(BrandId brandId, String name, Long price) {
        if (name == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 이름은 비어 있을 수 없습니다.");
        }
        if (brandId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 가겨은 비어 있을 수 없습니다.");
        }

        this.brandId = brandId;
        this.name = name;
        this.price = price;
    }

    public void like() {
        this.likeCount++;
    }

    public void unlike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
