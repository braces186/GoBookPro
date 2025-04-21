package com.ticketbooking.service.impl;

import com.ticketbooking.dto.VenueDTO;
import com.ticketbooking.dto.CreateVenueRequest;
import com.ticketbooking.model.Venue;
import com.ticketbooking.model.User;
import com.ticketbooking.repository.VenueRepository;
import com.ticketbooking.repository.UserRepository;
import com.ticketbooking.service.VenueService;
import com.ticketbooking.util.VenueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueMapper venueMapper;

    @Override
    public Page<VenueDTO> getVenues(String name, Double minPrice, Double maxPrice, Integer capacity, Pageable pageable) {
        Specification<Venue> spec = (root, query, cb) -> {
            if (name != null && !name.isEmpty()) {
                query.where(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (minPrice != null) {
                query.where(cb.greaterThanOrEqualTo(root.get("pricePerHour"), minPrice));
            }
            if (maxPrice != null) {
                query.where(cb.lessThanOrEqualTo(root.get("pricePerHour"), maxPrice));
            }
            if (capacity != null) {
                query.where(cb.greaterThanOrEqualTo(root.get("capacity"), capacity));
            }
            return query.getRestriction();
        };

        return venueRepository.findAll(spec, pageable).map(venueMapper::toDTO);
    }

    @Override
    public VenueDTO createVenue(Long merchantId, CreateVenueRequest request) {
        User merchant = userRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("商家不存在"));

        Venue venue = new Venue();
        venue.setName(request.getName());
        venue.setLocation(request.getLocation());
        venue.setDescription(request.getDescription());
        venue.setCapacity(request.getCapacity());
        venue.setPricePerHour(request.getPricePerHour());
        venue.setMerchant(merchant);

        return venueMapper.toDTO(venueRepository.save(venue));
    }

    @Override
    public VenueDTO getVenueById(Long id) {
        return venueMapper.toDTO(venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("場地不存在")));
    }

    @Override
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(venueMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VenueDTO> getVenuesByMerchant(Long merchantId) {
        User merchant = userRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("商家不存在"));
        return venueRepository.findByMerchant(merchant).stream()
                .map(venueMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VenueDTO updateVenue(Long id, VenueDTO venueDTO) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("場地不存在"));

        venue.setName(venueDTO.getName());
        venue.setLocation(venueDTO.getLocation());
        venue.setDescription(venueDTO.getDescription());
        venue.setCapacity(venueDTO.getCapacity());
        venue.setPricePerHour(venueDTO.getPricePerHour());

        return venueMapper.toDTO(venueRepository.save(venue));
    }

    @Override
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("場地不存在");
        }
        venueRepository.deleteById(id);
    }
} 