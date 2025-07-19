package com.loopers.application.point;

public class PointCommand {
    public record ChargeCommand(
            String userId,
            int point
    ) {
    }
}
