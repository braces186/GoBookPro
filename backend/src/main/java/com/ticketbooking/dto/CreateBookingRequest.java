package com.ticketbooking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {
    @NotNull(message = "場地ID不能為空")
    private Long venueId;

    @NotNull(message = "開始時間不能為空")
    private LocalDateTime startTime;

    @NotNull(message = "結束時間不能為空")
    private LocalDateTime endTime;
} 