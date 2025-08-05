package com.loopers.domain.like;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    
    @Transactional
    public void like(final LikeCommand.Like command) {
        final Like like = new Like(command.userId(), command.target());

        if (likeRepository.existsBy(like.getUserId(), like.getLikeTarget())) {
            return;
        }

        likeRepository.save(like);
    }

    @Transactional
    public void unlike(final LikeCommand.Unlike command) {
        likeRepository.findBy(command.userId(), command.target())
                .ifPresent(likeRepository::delete);
    }

    public List<LikeInfo> getProductLikes(final LikeCommand.GetLikeProducts command) {
        return likeRepository.findAllBy(command.userId(), command.targetType()).stream()
                .map(LikeInfo::from)
                .toList();
    }

}
