package com.loopers.application.point;

public class PointCommand {
    public record Charge(
            String userId,
            int point
    ) {
    }

    public record Use(
            String userId,
            Integer usePoint
    ) {

    }
}
