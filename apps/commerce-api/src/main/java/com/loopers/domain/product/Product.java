package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
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
        this.stock -= quantity;
        return this;
    }
}
