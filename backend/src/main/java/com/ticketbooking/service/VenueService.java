package com.ticketbooking.service;

import com.ticketbooking.dto.VenueDTO;
import com.ticketbooking.dto.CreateVenueRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface VenueService {
    Page<VenueDTO> getVenues(String name, Double minPrice, Double maxPrice, Integer capacity, Pageable pageable);
    VenueDTO createVenue(Long merchantId, CreateVenueRequest request);
    VenueDTO getVenueById(Long id);
    List<VenueDTO> getAllVenues();
    List<VenueDTO> getVenuesByMerchant(Long merchantId);
    VenueDTO updateVenue(Long id, VenueDTO venueDTO);
    void deleteVenue(Long id);
} 