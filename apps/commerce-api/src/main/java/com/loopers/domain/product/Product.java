package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    private Long brandId;
    private String name;
    private Integer price;
    private Integer stock;


    public Product(String name, int price, int stock, Long brand) {
        ProductValidator.validate(stock, price);
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.brandId = brand;
    }

    public Product decreaseStock(Integer quantity) {
        if (quantity <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "재고 차감 수량은 0보다 커야 합니다.");
        }

        if (stock < quantity) {
            throw new CoreException(ErrorType.BAD_REQUEST, "보유 재고가 부족합니다.");
        }

        this.stock -= quantity;
        return this;
    }
}
