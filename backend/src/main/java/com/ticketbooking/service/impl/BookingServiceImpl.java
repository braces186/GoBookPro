package com.ticketbooking.service.impl;

import com.ticketbooking.dto.BookingDTO;
import com.ticketbooking.dto.CreateBookingRequest;
import com.ticketbooking.model.Booking;
import com.ticketbooking.model.BookingStatus;
import com.ticketbooking.model.User;
import com.ticketbooking.model.Venue;
import com.ticketbooking.repository.BookingRepository;
import com.ticketbooking.repository.UserRepository;
import com.ticketbooking.repository.VenueRepository;
import com.ticketbooking.service.BookingService;
import com.ticketbooking.util.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Override
    public BookingDTO createBooking(String username, CreateBookingRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("使用者不存在"));

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("場地不存在"));

        // 檢查時間是否有效
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("開始時間不能晚於結束時間");
        }

        // 計算總價
        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());
        long hours = duration.toHours();
        double totalPrice = venue.getPricePerHour() * hours;

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVenue(venue);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        return bookingMapper.toDTO(bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("預訂不存在")));
    }

    @Override
    public List<BookingDTO> getUserBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("使用者不存在"));
        return bookingRepository.findByUser(user).stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getVenueBookings(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("場地不存在"));
        return bookingRepository.findByVenue(venue).stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("預訂不存在"));
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("預訂不存在"));
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingMapper.toDTO(bookingRepository.save(booking));
    }
} 