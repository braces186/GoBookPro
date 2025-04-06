package com.ticketbooking.util;

import com.ticketbooking.dto.VenueDTO;
import com.ticketbooking.model.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {
    
    public VenueDTO toDTO(Venue venue) {
        if (venue == null) {
            return null;
        }
        
        VenueDTO dto = new VenueDTO();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setLocation(venue.getLocation());
        dto.setDescription(venue.getDescription());
        dto.setCapacity(venue.getCapacity());
        dto.setPricePerHour(venue.getPricePerHour());
        dto.setMerchantId(venue.getMerchant().getId());
        dto.setCreatedAt(venue.getCreatedAt());
        dto.setUpdatedAt(venue.getUpdatedAt());
        return dto;
    }
} 