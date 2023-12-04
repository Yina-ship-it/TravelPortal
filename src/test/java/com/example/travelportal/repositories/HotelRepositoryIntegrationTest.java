package com.example.travelportal.repositories;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    @Test
    void findAllByCountry_IdAndNameFragmentOrderByStarsDesc(){
        // Arrange
        hotelRepository.deleteAll();
        Country country = Country.builder().name("CountryTest").capital("CapitalTest").build();
        entityManager.persist(country);

        List<Hotel> hotels = new ArrayList<>(List.of(
                Hotel.builder().name("HotelTest1").country(country).stars(5).build(),
                Hotel.builder().name("HotelTest2").country(country).stars(3).build(),
                Hotel.builder().name("HotelTest3").country(country).stars(1).build(),
                Hotel.builder().name("HotelTest4").country(country).stars(2).build(),
                Hotel.builder().name("HotelTest5").country(country).stars(4).build(),
                Hotel.builder().name("HotelTest6").country(country).stars(3).build(),
                Hotel.builder().name("HotelTest7").country(country).stars(3).build(),
                Hotel.builder().name("HotelTest8").country(country).stars(4).build()
        ));
        Collections.shuffle(hotels);
        for (Hotel hotel : hotels) {
            entityManager.persist(hotel);
        }

        hotels.sort((o1, o2) -> {
            int starsComparison = Integer.compare(o2.getStars(), o1.getStars());
            if (starsComparison != 0) {
                return starsComparison;
            }
            return o1.getName().compareTo(o2.getName());
        });

        // Act
        List<Hotel> result = hotelRepository
                .findAllByCountry_IdAndNameFragmentOrderByStarsDesc(country.getId(), "TesT");

        // Assert
        assertNotNull(result);
        assertEquals(hotels, result);
    }
}