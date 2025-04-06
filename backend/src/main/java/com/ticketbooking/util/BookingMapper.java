package com.ticketbooking.util;

import com.ticketbooking.dto.BookingDTO;
import com.ticketbooking.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    
    public BookingDTO toDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setVenueId(booking.getVenue().getId());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        return dto;
    }
} 