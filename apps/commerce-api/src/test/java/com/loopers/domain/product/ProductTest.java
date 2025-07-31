package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.domain.brand.Brand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("상품을 생성 할 때,")
    @Nested
    class Create {
        @DisplayName("가격이 음수면, 400 Bad Request를 반환한다.")
        @Test
        void fail400BadRequest_withNegtivePrice() {
            CoreException coreException = assertThrows(CoreException.class, () ->
                    Product.create(
                            "TEST", -1, 1_000, new Brand("TEST")
                    )
            );

            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("재고가 음수면, 400 Bad Request를 반환한다.")
        @Test
        void fail400BadRequest_withNegtiveStock() {
            CoreException coreException = assertThrows(CoreException.class, () ->
                    Product.create(
                            "TEST", 1_000, -1, new Brand("TEST")
                    )
            );
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

    @DisplayName("상품 주문 시,")
    @Nested
    class Order {
        @Test
        void 상품이_갖고있는_재고보다_차감되는_양이_클_경우_400_BadRequest를_반환한다() {
            Product product = Product.create(
                    "TEST", 1_000, 10, new Brand("TEST")
            );

            CoreException coreException = assertThrows(CoreException.class, () -> product.decreaseStock(20));

            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @Test
        void 상품의_재고를_차감하면_요청된_양_만큼_재고가_빠진다() {
            Product product = Product.create(
                    "TEST", 1_000, 10, new Brand("TEST")
            );
            Product decreasedProduct = product.decreaseStock(5);
            assertThat(decreasedProduct.getStock()).isEqualTo(5);
        }
    }
}
