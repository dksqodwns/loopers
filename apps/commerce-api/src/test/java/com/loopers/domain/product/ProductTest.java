package com.loopers.domain.product;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("재고 차감 시,")
    @Nested
    class DecreaseStock {
        @DisplayName("재고 차감 수량이 0보다 작거나 같으면, 실패한다.")
        @Test
        void decreaseStock_whenQuantityIsZeroOrNegative() {
            // given
            Product product = new Product("Test Product", 10000, 10, 1L);

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> product.decreaseStock(0));

            // then
            Assertions.assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("보유 재고가 부족하면, 실패한다.")
        @Test
        void decreaseStock_whenInsufficientStock() {
            // given
            Product product = new Product("Test Product", 10000, 5, 1L);

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> product.decreaseStock(10));

            // then
            Assertions.assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("정상적인 요청이면, 재고가 차감된다.")
        @Test
        void decreaseStock_whenValidRequest() {
            // given
            Product product = new Product("Test Product", 10000, 10, 1L);

            // when
            product.decreaseStock(3);

            // then
            Assertions.assertThat(product.getStock()).isEqualTo(7);
        }
    }
}
