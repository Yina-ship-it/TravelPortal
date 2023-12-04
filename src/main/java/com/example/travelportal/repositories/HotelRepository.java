package com.example.travelportal.repositories;

import com.example.travelportal.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByCountry_IdAndName(long countryId, String hotelName);
    int countByCountry_Id(long countryId);

    @Query("""
            SELECT h FROM Hotel h
            WHERE h.country.id = :countryId
                AND LOWER(h.name) LIKE LOWER(CONCAT('%', :fragment, '%'))
            ORDER BY h.stars DESC, h.name ASC
            """)
    List<Hotel> findAllByCountry_IdAndNameFragmentOrderByStarsDesc(long countryId, String fragment);
}
