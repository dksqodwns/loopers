package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductLikeTest {

    @DisplayName("상품 좋아요 생성 시")
    @Nested
    class Create {
        @Test
        void 정상적인_좋아요가_등록된다() {
            UserCreateCommand command = new UserCreateCommand(
                    "test", "테스터", "test@test.com", Gender.MALE, LocalDate.of(1998, 1, 8)
            );
            User user = User.create(command);
            Product product = Product.create("테스트 상품", 1_000, 30, new Brand("테스트 브랜드"));
            ProductLike likedProduct = ProductLike.like(user, product);

            assertAll(
                    () -> assertThat(likedProduct).isNotNull(),
                    () -> assertThat(likedProduct.getUser().getUserId()).isEqualTo(user.getUserId()),
                    () -> assertThat(likedProduct.getProduct().getId()).isEqualTo(product.getId())
            );
        }
    }
}
