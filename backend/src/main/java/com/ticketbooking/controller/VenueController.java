package com.ticketbooking.controller;

import com.ticketbooking.dto.VenueDTO;
import com.ticketbooking.dto.CreateVenueRequest;
import com.ticketbooking.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/venues")
@Tag(name = "場地管理", description = "場地相關的 API")
public class VenueController extends BaseController {

    @Autowired
    private VenueService venueService;

    @GetMapping
    @Operation(summary = "取得場地列表（分頁）")
    public ResponseEntity<Map<String, Object>> getVenues(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<VenueDTO> venues = venueService.getVenues(name, minPrice, maxPrice, capacity, pageable);
            return success(venues);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "創建場地")
    public ResponseEntity<Map<String, Object>> createVenue(@RequestParam Long merchantId, @Valid @RequestBody CreateVenueRequest request) {
        try {
            VenueDTO venue = venueService.createVenue(merchantId, request);
            return success(venue);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "取得場地資訊")
    public ResponseEntity<Map<String, Object>> getVenueById(@PathVariable Long id) {
        try {
            VenueDTO venue = venueService.getVenueById(id);
            return success(venue);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/merchant/{merchantId}")
    @Operation(summary = "取得商家的所有場地")
    public ResponseEntity<Map<String, Object>> getVenuesByMerchant(@PathVariable Long merchantId) {
        try {
            List<VenueDTO> venues = venueService.getVenuesByMerchant(merchantId);
            return success(venues);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新場地資訊")
    public ResponseEntity<Map<String, Object>> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueDTO venue) {
        try {
            VenueDTO updatedVenue = venueService.updateVenue(id, venue);
            return success(updatedVenue);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除場地")
    public ResponseEntity<Map<String, Object>> deleteVenue(@PathVariable Long id) {
        try {
            venueService.deleteVenue(id);
            return success("場地已成功刪除");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
} 