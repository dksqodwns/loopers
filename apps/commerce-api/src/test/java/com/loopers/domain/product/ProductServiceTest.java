package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.loopers.application.product.ProductCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository mockProductRepository;

    @DisplayName("재고 차감 시,")
    @Nested
    class DecreaseStock {

        @DisplayName("보유 재고가 부족하면, CoreException을 반환한다.")
        @Test
        void throwCoreException_whenStockIsInsufficient() {
            // given
            Long productId = 1L;
            Integer currentStock = 5;
            Integer quantityToDecrease = 10;
            ProductCommand.DecreaseStock command = new ProductCommand.DecreaseStock(productId, quantityToDecrease);

            Product product = new Product("Test Product", 10000, currentStock, 1L);

            given(mockProductRepository.findByProductId(productId)).willReturn(Optional.of(product));

            CoreException coreException = Assertions.assertThrows(CoreException.class, () -> productService.decreaseStock(command));

            Assertions.assertAll(
                    () -> assertThatThrownBy(() -> productService.decreaseStock(command))
                            .isInstanceOf(CoreException.class)
                            .hasMessageContaining("보유 재고가 부족합니다."),

                    () -> assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
            );

        }

        @DisplayName("정상적인 요청이면, 재고가 차감된다.")
        @Test
        void decreaseStock_whenValidRequest() {
            // given
            Long productId = 1L;
            Integer currentStock = 10;
            Integer quantityToDecrease = 5;
            ProductCommand.DecreaseStock command = new ProductCommand.DecreaseStock(productId, quantityToDecrease);

            Product product = new Product("Test Product", 10000, currentStock, 1L);

            given(mockProductRepository.findByProductId(productId)).willReturn(Optional.of(product));
            given(mockProductRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            productService.decreaseStock(command);

            // then
            verify(mockProductRepository, times(1)).save(any(Product.class));
            assertThat(product.getStock()).isEqualTo(currentStock - quantityToDecrease);
        }
    }
}
