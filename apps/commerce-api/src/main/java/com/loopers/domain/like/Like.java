package com.loopers.domain.like;

import com.loopers.application.like.LikeCommand;
import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Table(name = "product_like")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {
    private String userId;
    private Long productId;

    private Like(String userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public static Like create(LikeCommand.Like command) {
        return new Like(command.userId(), command.productId());
    }
}
