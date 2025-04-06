package com.ticketbooking.repository;

import com.ticketbooking.model.Booking;
import com.ticketbooking.model.BookingStatus;
import com.ticketbooking.model.User;
import com.ticketbooking.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByVenueId(Long venueId);
    List<Booking> findByVenueIdAndStartTimeBetween(Long venueId, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByUser(User user);
    List<Booking> findByVenue(Venue venue);
} 