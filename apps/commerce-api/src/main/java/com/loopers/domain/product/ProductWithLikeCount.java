package com.loopers.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductWithLikeCount {
    private Product product;
    private long likeCount;
}
