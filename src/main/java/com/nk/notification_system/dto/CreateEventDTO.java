package com.nk.notification_system.dto;

import com.nk.notification_system.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEventDTO(
        @NotNull(message = "eventType cannot be NULL") EventType eventType,
        @NotBlank(message = "eventName cannot be NULL") String eventName,
        @NotBlank(message = "event message cannot be NULL") String payload) {
}
