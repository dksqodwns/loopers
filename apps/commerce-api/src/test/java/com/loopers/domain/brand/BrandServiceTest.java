package com.loopers.domain.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.loopers.application.brand.BrandCommand;
import com.loopers.application.brand.BrandInfo;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository mockBrandRepository;

    @DisplayName("브랜드 정보를 조회 할 경우,")
    @Nested
    class Get {

        @DisplayName("존재하지 않는 브랜드 ID가 주어지면, Optional을 반환한다.")
        @Test
        void returnOptional_whenNotExistsBrandId() {
            given(mockBrandRepository.findByBrandId(anyLong()))
                    .willReturn(Optional.empty());

            Optional<BrandInfo> result = brandService.getBrandbyId(new BrandCommand.Get(777L));

            assertThat(result).isEmpty();
        }
    }
}
