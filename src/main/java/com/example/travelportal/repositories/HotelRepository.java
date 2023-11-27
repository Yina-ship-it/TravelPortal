package com.example.travelportal.repositories;

import com.example.travelportal.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByCountry_IdAndName(long countryId, String hotelName);
    int countByCountry_Id(long countryId);
}
