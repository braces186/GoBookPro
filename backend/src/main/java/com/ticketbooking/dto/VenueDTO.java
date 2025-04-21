package com.ticketbooking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VenueDTO {
    private Long id;
    private String name;
    private String location;
    private String description;
    private Integer capacity;
    private Double pricePerHour;
    private Long merchantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 