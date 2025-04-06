package com.ticketbooking.controller;

import com.ticketbooking.dto.BookingDTO;
import com.ticketbooking.dto.CreateBookingRequest;
import com.ticketbooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "預訂管理", description = "預訂相關的 API")
public class BookingController extends BaseController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @Operation(summary = "創建預訂")
    public ResponseEntity<Map<String, Object>> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            BookingDTO booking = bookingService.createBooking(username, request);
            return success(booking);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "獲取預訂詳情")
    public ResponseEntity<Map<String, Object>> getBooking(@PathVariable Long id) {
        try {
            BookingDTO booking = bookingService.getBookingById(id);
            return success(booking);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/user")
    @Operation(summary = "獲取用戶的所有預訂")
    public ResponseEntity<Map<String, Object>> getUserBookings(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<BookingDTO> bookings = bookingService.getUserBookings(username);
            return success(bookings);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/venue/{venueId}")
    @Operation(summary = "獲取場地的所有預訂")
    public ResponseEntity<Map<String, Object>> getVenueBookings(@PathVariable Long venueId) {
        try {
            List<BookingDTO> bookings = bookingService.getVenueBookings(venueId);
            return success(bookings);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消預訂")
    public ResponseEntity<Map<String, Object>> cancelBooking(@PathVariable Long id) {
        try {
            BookingDTO cancelledBooking = bookingService.cancelBooking(id);
            return success(cancelledBooking);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "確認預訂")
    public ResponseEntity<Map<String, Object>> confirmBooking(@PathVariable Long id) {
        try {
            BookingDTO confirmedBooking = bookingService.confirmBooking(id);
            return success(confirmedBooking);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
} 