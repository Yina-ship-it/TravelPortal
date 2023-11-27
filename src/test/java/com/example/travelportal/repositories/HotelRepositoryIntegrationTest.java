package com.example.travelportal.repositories;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HotelRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void findByCountry_IdAndName() {
        // Arrange
        Country country = Country.builder().name("CountryTest").capital("CapitalTest").build();
        entityManager.persist(country);
        Hotel hotel = Hotel.builder().name("HotelTest").country(country).stars(5).build();
        entityManager.persist(hotel);
        entityManager.flush();

        // Act
        Hotel result = hotelRepository.findByCountry_IdAndName(country.getId(), hotel.getName()).orElse(null);

        // Assert
        assertNotNull(result);
        assertEquals(hotel, result);
    }

    @Test
    void countByCountry_Id(){
        // Arrange
        Country country = Country.builder().name("CountryTest").capital("CapitalTest").build();
        entityManager.persist(country);
        Hotel hotel1 = Hotel.builder().name("HotelTest1").country(country).stars(5).build();
        Hotel hotel2 = Hotel.builder().name("HotelTest2").country(country).stars(5).build();
        Hotel hotel3 = Hotel.builder().name("HotelTest3").country(country).stars(5).build();
        entityManager.persist(hotel1);
        entityManager.persist(hotel2);
        entityManager.persist(hotel3);
        entityManager.flush();

        // Act
        int result = hotelRepository.countByCountry_Id(country.getId());

        // Assert
        assertEquals(3, result);
    }
}