package com.example.travelportal.services.hotel;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DefaultHotelServiceTest {

    @Mock
    HotelRepository hotelRepository;

    @InjectMocks
    DefaultHotelService hotelService;

    @Test
    void getAllHotels_WhenHotelsExist_ShouldReturnListOfHotels() {
        // Arrange
        Country country = new Country(1L, "Россия", "Москва");
        List<Hotel> hotels = List.of(new Hotel(1L, "Отель1", country, 5, null),
                new Hotel(2L, "Отель 2", country, 4, null));

        doReturn(hotels).when(hotelRepository).findAll();

        // Act
        List<Hotel> result = hotelService.getAllHotels();

        // Assert
        assertNotNull(result);
        assertEquals(hotels, result);
    }

    @Test
    void getAllHotels_WhenNoHotelsExist_ShouldReturnListOfHotels() {
        // Arrange
        List<Hotel> hotels = List.of();

        doReturn(hotels).when(hotelRepository).findAll();

        // Act
        List<Hotel> result = hotelService.getAllHotels();

        // Assert
        assertNotNull(result);
        assertEquals(hotels, result);
    }

    @Test
    void getHotelById_WhenExistingHotel_ShouldReturnHotel() {
        // Arrange
        long hotelId = 1;
        Hotel hotel = new Hotel(hotelId, "Отель1", new Country(1L, "Россия", "Москва"), 5, null);
        doReturn(Optional.of(hotel)).when(hotelRepository).findById(hotelId);

        // Act
        Hotel result = hotelService.getHotelById(hotelId);

        // Assert
        assertNotNull(result);
        assertEquals(hotel, result);
    }

    @Test
    void getHotelById_WhenNonExistingHotel_ShouldReturnHotel() {
        // Arrange
        long hotelId = 1;
        doReturn(Optional.empty()).when(hotelRepository).findById(hotelId);

        // Act
        Hotel result = hotelService.getHotelById(hotelId);

        // Assert
        assertNull(result);
    }

    @Test
    void updateHotel() {
    }

    @Test
    void saveHotel() {
    }

    @Test
    void deleteHotelById() {
    }
}