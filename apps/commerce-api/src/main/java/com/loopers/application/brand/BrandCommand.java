package com.loopers.application.brand;

public class BrandCommand {
    public record Get(
            Long brandId
    ) {
    }

    public record Create(
            String name
    ) {
    }

    public record ProductId(
            Long productId
    ) {

    }

}
