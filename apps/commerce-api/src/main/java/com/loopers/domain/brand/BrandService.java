package com.loopers.domain.brand;

import com.loopers.application.brand.BrandCommand;
import com.loopers.application.brand.BrandInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public Optional<BrandInfo> getBrand(BrandCommand.Get command) {
        return this.brandRepository.findByBrandId(command.brandId())
                .flatMap(brand -> Optional.of(BrandInfo.from(brand)));
    }
}
