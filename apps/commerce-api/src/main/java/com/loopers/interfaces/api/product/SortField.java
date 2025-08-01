package com.loopers.interfaces.api.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortField {
    CREATED_AT("createdAt"),
    PRICE("price"),
    LIKE_COUNT("likeCount");

    private final String field;

    public static SortField fromField(String field) {
        for (SortField sortField : SortField.values()) {
            if (sortField.getField().equalsIgnoreCase(field)) {
                return sortField;
            }
        }
        return CREATED_AT;
    }
}
