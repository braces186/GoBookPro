package com.ticketbooking.dto;

import com.ticketbooking.model.BookingStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long venueId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 