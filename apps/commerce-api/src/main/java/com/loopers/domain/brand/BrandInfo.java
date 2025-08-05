package com.loopers.domain.brand;

public record BrandInfo(Long id, String name) {

    public static BrandInfo from(Brand brand) {
        return new BrandInfo(brand.getId(), brand.getName());
    }
}
