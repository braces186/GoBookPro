package com.ticketbooking.service;

import com.ticketbooking.dto.VenueDTO;
import com.ticketbooking.dto.CreateVenueRequest;
import java.util.List;

public interface VenueService {
    VenueDTO createVenue(Long merchantId, CreateVenueRequest request);
    VenueDTO getVenueById(Long id);
    List<VenueDTO> getAllVenues();
    List<VenueDTO> getVenuesByMerchant(Long merchantId);
    VenueDTO updateVenue(Long id, VenueDTO venueDTO);
    void deleteVenue(Long id);
} 