package com.loopers.domain.like;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.loopers.application.like.LikeCommand;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository mockLikeRepository;

    @DisplayName("좋아요 등록 시,")
    @Nested
    class PostLike {

        @DisplayName("기존에 좋아요가 없으면, save가 호출된다.")
        @Test
        void should_call_save_when_like_does_not_exist() {
            // given
            LikeCommand.Like command = new LikeCommand.Like("testUser", 1L);
            given(mockLikeRepository.exists(any(Like.class))).willReturn(false);

            // when
            likeService.like(command);

            // then
            verify(mockLikeRepository, times(1)).save(any(Like.class));
        }

        @DisplayName("이미 좋아요가 존재하면, save가 호출되지 않는다.")
        @Test
        void should_not_call_save_when_like_already_exists() {
            // given
            LikeCommand.Like command = new LikeCommand.Like("testUser", 1L);
            given(mockLikeRepository.exists(any(Like.class))).willReturn(true);

            // when
            likeService.like(command);

            // then
            verify(mockLikeRepository, never()).save(any(Like.class));
        }
    }

    @DisplayName("좋아요 취소 시,")
    @Nested
    class Deletelike {

        @DisplayName("기존에 좋아요가 존재하면, delete가 호출된다.")
        @Test
        void should_call_delete_when_like_exists() {
            // given
            String userId = "testUser";
            Long productId = 1L;
            Like existingLike = Like.create(new LikeCommand.Like(userId, productId));
            given(mockLikeRepository.findByUserIdAndProductId(userId, productId)).willReturn(Optional.of(existingLike));

            // when
            likeService.unlike(userId, productId);

            // then
            verify(mockLikeRepository, times(1)).delete(existingLike);
        }

        @DisplayName("좋아요가 존재하지 않으면, delete가 호출되지 않는다.")
        @Test
        void should_not_call_delete_when_like_does_not_exist() {
            // given
            String userId = "testUser";
            Long productId = 1L;
            given(mockLikeRepository.findByUserIdAndProductId(userId, productId)).willReturn(Optional.empty());

            // when
            likeService.unlike(userId, productId);

            // then
            verify(mockLikeRepository, never()).delete(any(Like.class));
        }
    }
}
