package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.Getter;

@Getter
public class Product {

    private Long id;
    private String name;
    private int price;
    private int stock;
    private Brand brand;
    private int like_count;

    private Product(
            String name, int price, int stock, Brand brand
    ) {
        ProductValidator.validate(price, stock);
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.like_count = 0;
    }

    public static Product create(
            String name, int price, int stock, Brand brand
    ) {
        return new Product(name, price, stock, brand);
    }

    public Product decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품의 재고는 음수가 될 수 없습니다. productId: " + this.id);
        }
        this.stock -= quantity;
        return this;
    }
}
