package com.example.travelportal.services.hotel;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.repositories.HotelRepository;
import com.example.travelportal.services.country.CountryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultHotelServiceTest {

    @Mock
    HotelRepository hotelRepository;

    @Mock
    CountryService countryService;

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
    void updateHotel_WithValidData_ShouldCreateHotel() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = new Hotel(1L, "Отель1", country, 4, "https://hotel1.com");
        Hotel newHotel = new Hotel(1L, "Отель1", country, 5, "https://hotel1.com");

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(oldHotel))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & assert
        assertDoesNotThrow(() -> hotelService.updateHotel(newHotel));
        verify(this.hotelRepository).save(newHotel);

    }

    @Test
    void updateHotel_WithHotelNotFound_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel newHotel = new Hotel(1L, "Отель1", country, 5, "https://hotel1.com");

        doReturn(Optional.empty()).when(hotelRepository).findById(newHotel.getId());

        // Act & assert
        assertThrows(EntityNotFoundException.class,
                () -> hotelService.updateHotel(newHotel));
    }

    @Test
    void updateHotel_WithDuplicateNameInCountry_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = new Hotel(1L, "Отель2", country, 4, "https://hotel2.com");
        Hotel otherHotel = new Hotel(2L, "Отель1", country, 4, "https://hotel1.com");
        Hotel newHotel = new Hotel(1L, "Отель1", country, 4, "https://hotel1.com");

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(otherHotel))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.updateHotel(newHotel),
                "A hotel with the name '" + newHotel.getName() + "' already exists in the country.");
    }

    @Test
    void updateHotel_WithCountryWithoutId_ShouldThrowException() {
        // Arrange
        Country country = Country.builder().name("Франция").capital("Париж").build();
        Hotel oldHotel = Hotel.builder().id(1L).name("Отель2").stars(4).build();
        Hotel newHotel = new Hotel(1L, "Отель1", country, 5, "https://hotel1.com");

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());

        // Act & assert
        assertThrows(EntityNotFoundException.class,
                () -> hotelService.updateHotel(newHotel));
    }

    @Test
    void updateHotel_WithCountryNotFound_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = Hotel.builder().id(1L).name("Отель2").stars(4).build();
        Hotel newHotel = new Hotel(1L, "Отель1", country, 5, "https://hotel1.com");

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(null).when(countryService).getCountryById(country.getId());

        // Act & assert
        assertThrows(EntityNotFoundException.class,
                () -> hotelService.updateHotel(newHotel));
    }

    @Test
    void updateHotel_WithInvalidStars_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = new Hotel(1L, "Отель1", country, 4, "https://hotel1.com");
        Hotel newHotel = new Hotel(1L, "Отель1", country, 7, "https://hotel1.com");

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(oldHotel))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.updateHotel(newHotel),"Invalid hotel stars");
    }

    @Test
    void updateHotel_WhenHotelInputWithoutStars_ShouldThrowException() {
        // Arrange
        String tooLongName = "A".repeat(256);
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = new Hotel(1L, "Отель1", country, 4, "https://hotel1.com");
        Hotel newHotel = Hotel.builder()
                .id(1L)
                .name("Отель1")
                .country(country)
                .website("https://hotel1.com").build();

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(oldHotel))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.updateHotel(newHotel),"Invalid hotel stars");
    }

    @Test
    void updateHotel_WhenNameExceedsMaxLength_ShouldThrowException() {
        // Arrange
        String tooLongName = "A".repeat(256);
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = new Hotel(1L, "Отель1", country, 4, "https://hotel1.com");
        Hotel newHotel = new Hotel(1L, tooLongName, country, 4, "https://hotel1.com");

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(oldHotel))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.updateHotel(newHotel),"Invalid hotel name");
    }

    @Test
    void updateHotel_WhenHotelInputWithoutName_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = new Hotel(1L, "Отель1", country, 4, "https://hotel1.com");
        Hotel newHotel = Hotel.builder()
                .id(1L)
                .country(country)
                .stars(4)
                .website("https://hotel1.com").build();

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(oldHotel))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.updateHotel(newHotel),"Invalid hotel name");
    }

    @Test
    void updateHotel_WithInvalidWebsite_ShouldThrowException() {
        // Arrange
        String invalidWebsite = "abc";
        Country country = new Country(1L, "Франция", "Париж");
        Hotel oldHotel = new Hotel(1L, "Отель1", country, 4, "https://hotel1.com");
        Hotel newHotel = new Hotel(1L, "Отель1", country, 4, invalidWebsite);

        doReturn(Optional.of(oldHotel)).when(hotelRepository).findById(newHotel.getId());
        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(oldHotel))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.updateHotel(newHotel),"Invalid hotel website");
    }

    @Test
    void saveHotel_WithValidData_ShouldCreateHotel() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel newHotel = Hotel.builder()
                .name("Отель1")
                .country(country)
                .stars(5)
                .website("https://hotel1.com")
                .build();

        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.empty())
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & assert
        assertDoesNotThrow(() -> hotelService.saveHotel(newHotel));
        verify(this.hotelRepository).save(newHotel);

    }

    @Test
    void saveHotel_WithDuplicateNameInCountry_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel newHotel = Hotel.builder()
                .name("Отель1")
                .country(country)
                .stars(5)
                .website("https://hotel1.com")
                .build();

        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.of(new Hotel(2L, "Отель1", country, 4, null)))
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.saveHotel(newHotel),
                "A hotel with the name '" + newHotel.getName() + "' already exists in the country.");
    }

    @Test
    void saveHotel_WithCountryNotFound_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel newHotel = Hotel.builder()
                .name("Отель1")
                .country(country)
                .stars(5)
                .website("https://hotel1.com")
                .build();

        doReturn(null).when(countryService).getCountryById(country.getId());

        // Act & assert
        assertThrows(EntityNotFoundException.class,
                () -> hotelService.saveHotel(newHotel));
    }

    @Test
    void saveHotel_WhenNameExceedsMaxLength_ShouldThrowException() {
        // Arrange
        String tooLongName = "A".repeat(256);
        Country country = new Country(1L, "Франция", "Париж");
        Hotel newHotel = Hotel.builder()
                .name(tooLongName)
                .country(country)
                .stars(5)
                .website("https://hotel1.com")
                .build();

        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.empty())
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.saveHotel(newHotel),"Invalid hotel name");
    }

    @Test
    void saveHotel_WithInvalidStars_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L, "Франция", "Париж");
        Hotel newHotel = Hotel.builder()
                .name("Отель1")
                .country(country)
                .stars(7)
                .website("https://hotel1.com")
                .build();

        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.empty())
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.saveHotel(newHotel),"Invalid hotel stars");
    }

    @Test
    void saveHotel_WithInvalidWebsite_ShouldThrowException() {
        // Arrange
        String invalidWebsite = "abc";
        Country country = new Country(1L, "Франция", "Париж");
        Hotel newHotel = Hotel.builder()
                .name("Отель1")
                .country(country)
                .stars(5)
                .website(invalidWebsite)
                .build();

        doReturn(country).when(countryService).getCountryById(country.getId());
        doReturn(Optional.empty())
                .when(hotelRepository).findByCountry_IdAndName(country.getId(), newHotel.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> hotelService.saveHotel(newHotel),"Invalid hotel website");
    }

    @Test
    void deleteHotelById_ShouldDeleteHotel() {
        // Arrange
        long hotelId = 1;
        Hotel hotel = new Hotel(hotelId, "Отель1", new Country(1L, "Россия", "Москва"), 5, null);

        doReturn(Optional.of(hotel)).when(hotelRepository).findById(hotelId);

        // Act & Assert
        assertDoesNotThrow(() -> hotelService.deleteHotelById(hotelId));
        verify(this.hotelRepository).delete(hotel);
    }

    @Test
    void deleteHotelById_WhenHotelNotFound_ShouldDoNothing() {
        // Arrange
        long hotelId = 1;

        doReturn(Optional.empty()).when(hotelRepository).findById(hotelId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> hotelService.deleteHotelById(hotelId));
    }

    @Test
    void getHotelCountByCountryId_ShouldHotelCountInCountry(){
        // Arrange
        Country country = new Country(1L, "Россия", "Москва");
        List<Hotel> hotels = List.of(new Hotel(1L, "Отель1", country, 5, null),
                new Hotel(2L, "Отель 2", country, 4, null));

        doReturn(hotels.size()).when(hotelRepository).countByCountry_Id(country.getId());

        // Act
        int result = hotelService.getHotelCountByCountryId(country.getId());

        // Assert
        assertEquals(hotels.size(), result);
    }
}