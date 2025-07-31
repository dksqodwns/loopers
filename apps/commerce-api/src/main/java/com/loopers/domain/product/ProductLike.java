package com.loopers.domain.product;

import com.loopers.domain.user.User;
import lombok.Getter;

@Getter
public class ProductLike {
    private User user;
    private Product product;

    private ProductLike(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public static ProductLike like(User user, Product product) {
        return new ProductLike(user, product);
    }
}
