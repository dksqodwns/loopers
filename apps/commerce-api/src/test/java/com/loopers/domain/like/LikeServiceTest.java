package com.loopers.domain.like;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Nested
    @DisplayName("like 메서드는")
    class Like {

        @Test
        @DisplayName("이미 좋아요한 경우, 아무것도 하지 않는다")
        void like_alreadyLiked_doNothing() {
            // given
            LikeCommand.Like command = new LikeCommand.Like(1L, new LikeTarget(LikeTarget.TargetType.PRODUCT, 1L));
            given(likeRepository.existsBy(command.userId(), command.target())).willReturn(true);

            // when
            likeService.like(command);

            // then
            verify(likeRepository, never()).save(any(com.loopers.domain.like.Like.class));
        }

        @Test
        @DisplayName("좋아요하지 않은 경우, 좋아요를 저장한다")
        void like_notLiked_saveLike() {
            // given
            LikeCommand.Like command = new LikeCommand.Like(1L, new LikeTarget(LikeTarget.TargetType.PRODUCT, 1L));
            given(likeRepository.existsBy(command.userId(), command.target())).willReturn(false);

            // when
            likeService.like(command);

            // then
            verify(likeRepository).save(any(com.loopers.domain.like.Like.class));
        }
    }

    @Nested
    @DisplayName("unlike 메서드는")
    class Unlike {

        @Test
        @DisplayName("좋아요한 경우, 좋아요를 삭제한다")
        void unlike_liked_deleteLike() {
            // given
            LikeCommand.Unlike command = new LikeCommand.Unlike(1L, new LikeTarget(LikeTarget.TargetType.PRODUCT, 1L));
            given(likeRepository.findBy(command.userId(), command.target())).willReturn(Optional.of(new com.loopers.domain.like.Like(command.userId(), command.target())));

            // when
            likeService.unlike(command);

            // then
            verify(likeRepository).delete(any(com.loopers.domain.like.Like.class));
        }

        @Test
        @DisplayName("좋아요하지 않은 경우, 아무것도 하지 않는다")
        void unlike_notLiked_doNothing() {
            // given
            LikeCommand.Unlike command = new LikeCommand.Unlike(1L, new LikeTarget(LikeTarget.TargetType.PRODUCT, 1L));
            given(likeRepository.findBy(command.userId(), command.target())).willReturn(Optional.empty());

            // when
            likeService.unlike(command);

            // then
            verify(likeRepository, never()).delete(any(com.loopers.domain.like.Like.class));
        }
    }

    @Nested
    @DisplayName("getProductLikes 메서드는")
    class GetProductLikes {

        @Test
        @DisplayName("좋아요한 상품 목록을 반환한다")
        void getProductLikes_returnLikedProducts() {
            // given
            LikeCommand.GetLikeProducts command = new LikeCommand.GetLikeProducts(1L, LikeTarget.TargetType.PRODUCT);
            given(likeRepository.findAllBy(command.userId(), command.targetType())).willReturn(List.of(new com.loopers.domain.like.Like(1L, new LikeTarget(LikeTarget.TargetType.PRODUCT, 1L))));

            // when
            List<LikeInfo> result = likeService.getProductLikes(command);

            // then
            assertThat(result).hasSize(1);
        }
    }
}