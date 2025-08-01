package com.loopers.interfaces.api.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortField {
    CREATED_AT("createdAt");

    private final String field;
}
