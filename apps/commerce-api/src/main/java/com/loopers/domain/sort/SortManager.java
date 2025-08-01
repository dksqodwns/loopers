package com.loopers.domain.sort;

import org.springframework.data.domain.Sort;

public class SortManager {
    public static Sort productSort(String sortBy) {
        return switch (sortBy) {
            case "price_asc" -> Sort.by("price").ascending();
            case "likes_desc" -> Sort.by("likeCount").descending();
            default -> Sort.by("createdAt").descending();
        };
    }
}
