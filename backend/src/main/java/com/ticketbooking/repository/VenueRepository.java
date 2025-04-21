package com.ticketbooking.repository;

import com.ticketbooking.model.Venue;
import com.ticketbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {
    List<Venue> findByMerchant(User merchant);
    List<Venue> findByMerchantId(Long merchantId);
    List<Venue> findByLocationContaining(String location);
} 