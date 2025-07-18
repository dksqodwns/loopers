package com.loopers.application.point;

public class PointCommand {
    public record PointChargeCommand(
            String userId,
            int point
    ) { }
}
