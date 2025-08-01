package com.loopers.interfaces.api.brand;

import com.loopers.application.brand.BrandCommand;
import com.loopers.application.brand.BrandFacade;
import com.loopers.application.brand.BrandInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.brand.BrandDto.BrandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/brands")
public class BrandController implements BrandV1ApiSpec {
    private final BrandFacade brandFacade;

    @GetMapping("/{brandId}")
    public ApiResponse<BrandResponse> getBrand(@PathVariable Long brandId) {
        BrandCommand.Get command = new BrandCommand.Get(brandId);
        BrandInfo brandInfo = this.brandFacade.getBrand(command);
        BrandResponse response = BrandResponse.from(brandInfo);
        return ApiResponse.success(response);
    }
}
