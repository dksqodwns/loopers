package com.loopers.interfaces.api.brand;

import com.loopers.application.brand.BrandCommand.Create;
import com.loopers.application.brand.BrandInfo;
import jakarta.validation.constraints.NotNull;

public class BrandDto {

    public record BrandResponse(Long id, String name) {
        public static BrandResponse from(BrandInfo brandInfo) {
            return new BrandResponse(brandInfo.id(), brandInfo.name());
        }
    }

    public record BrandCreateRequest(
            @NotNull(message = "이름은 비어 있을 수 없습니다.")
            String name
    ) {

        public Create toCommand() {
            return new Create(
                    this.name
            );
        }
    }
}
