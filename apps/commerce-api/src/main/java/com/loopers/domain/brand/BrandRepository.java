package com.loopers.domain.brand;

import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> findByBrandId(Long brandId);

    Brand save(Brand brand);
}
