package com.nk.notification_system.dto;

public record UserResponse(
        Long userId,
        String name,
        String email
) {
}
