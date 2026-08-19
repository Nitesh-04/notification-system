package com.nk.notification_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSubRequest(
        @NotNull(message = "userId is required") Long userId,
        @NotBlank(message = "eventName is required") String eventName) {
}
