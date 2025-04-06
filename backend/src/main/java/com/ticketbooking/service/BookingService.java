package com.ticketbooking.service;

import com.ticketbooking.dto.BookingDTO;
import com.ticketbooking.dto.CreateBookingRequest;
import java.util.List;

public interface BookingService {
    BookingDTO createBooking(String username, CreateBookingRequest request);
    BookingDTO getBookingById(Long id);
    List<BookingDTO> getUserBookings(String username);
    List<BookingDTO> getVenueBookings(Long venueId);
    BookingDTO cancelBooking(Long id);
    BookingDTO confirmBooking(Long id);
} 