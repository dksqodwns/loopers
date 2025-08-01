package com.loopers.application.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandFacadeTest {
    @InjectMocks
    private BrandFacade brandFacade;

    @Mock
    private BrandService brandService;

    @DisplayName("브랜드 정보를 조회 할 때,")
    @Nested
    class Get {

        @DisplayName("존재하지 않는 브랜드 일 경우, 404 Not Found 에러를 반환한다.")
        @Test
        void return404NotFound_whenNotExistsBrandId() {
            given(brandService.getBrandbyId(new BrandCommand.Get(ArgumentMatchers.any())))
                    .willReturn(Optional.empty());

            CoreException coreException = Assertions.assertThrows(CoreException.class, () -> brandFacade.getBrand(new BrandCommand.Get(777L)));
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("존재하는 브랜드일 경우, BrandInfo가 반환된다.")
        @Test
        void returnBrandInfo_whenExistsBrandId() {
            BrandInfo brandInfo = BrandInfo.from(Brand.create("카미엔"));

            given(brandService.getBrandbyId(new BrandCommand.Get(1L))).willReturn(Optional.of(brandInfo));
            assertThat(brandFacade.getBrand(new BrandCommand.Get(1L))).isEqualTo(brandInfo);
        }
    }
}
