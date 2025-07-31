package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.Brand;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBrandById(Long brandId);
}
