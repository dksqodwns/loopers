package com.loopers.application.brand;

import com.loopers.domain.brand.BrandService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandFacade {
    private final BrandService brandService;

    public BrandInfo getBrand(BrandCommand.Get command) {
        return this.brandService.getBrand(command)
                .orElseThrow(
                        () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 브랜드를 찾을 수 없습니다. brandId: " + command.brandId())
                );
    }
}
