package com.loopers.domain.brand;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class BrandTest {

    @DisplayName("브랜드 생성 시,")
    @Nested
    class Create {

        @DisplayName("브랜드 명이 비어있으면, 400 Bad Request를 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void badRequest_whenInvalidRequset(String name) {
            CoreException coreException = Assertions.assertThrows(CoreException.class, () -> Brand.create(name));
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
