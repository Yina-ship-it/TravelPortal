package com.example.travelportal.services.country;

import com.example.travelportal.model.Country;
import com.example.travelportal.repositories.CountryRepository;
import com.example.travelportal.services.hotel.HotelService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCountryServiceTest {
    @Mock
    CountryRepository countryRepository;

    @Mock
    HotelService hotelService;

    @InjectMocks
    DefaultCountryService countryService;

    @Test
    void getAllCountries_WhenCountriesExist_ShouldReturnListOfCountries() {
        // Arrange
        List<Country> countries = List.of(new Country(1L, "Россия", "Москва"),
                new Country(2L, "Италия", "Рим"));

        doReturn(countries).when(countryRepository).findAll();

        // Act
        List<Country> result = countryService.getAllCountries();

        // Assert
        assertNotNull(result);
        assertEquals(countries, result);
    }

    @Test
    void getAllCountries_WhenNoCountriesExist_ShouldReturnListOfCountries() {
        // Arrange
        List<Country> countries = List.of();

        doReturn(countries).when(countryRepository).findAll();

        // Act
        List<Country> result = countryService.getAllCountries();

        // Assert
        assertNotNull(result);
        assertEquals(countries, result);
    }

    @Test
    void getCountryById_WhenExistingCountry_ShouldReturnCountry() {
        // Arrange
        long countryId = 1;
        Country country = new Country(countryId, "Россия", "Москва");
        doReturn(Optional.of(country)).when(countryRepository).findById(countryId);

        // Act
        Country result = countryService.getCountryById(countryId);

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }

    @Test
    void getCountryById_WhenNonExistingCountry_ShouldReturnNull() {
        // Arrange
        long countryId = 1;
        doReturn(Optional.empty()).when(countryRepository).findById(countryId);

        // Act
        Country result = countryService.getCountryById(countryId);

        // Assert
        assertNull(result);
    }

    @Test
    void updateCountry_WhenCapitalWithValidData_ShouldUpdateCountry() {
        // Arrange
        Country country = new Country(1L,"Франция", "Лондон");
        Country updatedCountry = new Country(1L, "Франция", "Париж");

        doReturn(Optional.of(country)).when(countryRepository).findByName(updatedCountry.getName());

        // Act & Assert
        assertDoesNotThrow(() -> countryService.updateCountry(updatedCountry));
        verify(this.countryRepository).save(updatedCountry);
    }

    @Test
    void updateCountry_WhenNameWithValidData_ShouldUpdateCountry() {
        // Arrange
        Country country = new Country(1L,"Франция", "Лондон");
        Country updatedCountry = new Country(1L, "Великобритания", "Лондон");

        doReturn(Optional.empty()).when(countryRepository).findByName(updatedCountry.getName());

        // Act & Assert
        assertDoesNotThrow(() -> countryService.updateCountry(updatedCountry));
        verify(this.countryRepository).save(updatedCountry);
    }

    @Test
    void updateCountry_WithDuplicateName_ShouldThrowException() {
        // Arrange
        Country country = new Country(1L,"Франция", "Лондон");
        Country updatedCountry = new Country(2L, "Франция", "Париж");

        doReturn(Optional.of(country)).when(countryRepository).findByName(updatedCountry.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> countryService.saveCountry(updatedCountry));

    }

    @Test
    void updateCountry_WhenNameExceedsMaxLength_ShouldThrowException() {
        // Arrange
        String tooLongName = "A".repeat(256);
        Country country = new Country(1L,"Франция", "Лондон");
        Country updatedCountry = new Country(1L, tooLongName, "Париж");

        lenient().when(countryRepository.findByName(updatedCountry.getName()))
                .thenReturn(Optional.of(country));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> countryService.saveCountry(updatedCountry),"Invalid country name");
    }

    @Test
    void updateCountry_WhenCapitalExceedsMaxLength_ShouldThrowException() {
        // Arrange
        String tooLongCapital = "A".repeat(129);
        Country country = new Country(1L,"Франция", "Лондон");
        Country updatedCountry = new Country(1L, "Франция", tooLongCapital);

        doReturn(Optional.of(country)).when(countryRepository).findByName(updatedCountry.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> countryService.saveCountry(updatedCountry), "Invalid country capital");
    }


    @Test
    void saveCountry_WithValidData_ShouldCreateCountry() {
        // Arrange
        Country newCountry = new Country();
        newCountry.setName("Франция");
        newCountry.setCapital("Париж");

        doReturn(Optional.empty()).when(countryRepository).findByName(newCountry.getName());

        // Act & assert
        assertDoesNotThrow(() -> countryService.saveCountry(newCountry));
        verify(this.countryRepository).save(newCountry);
    }

    @Test
    void saveCountry_WithDuplicateName_ShouldThrowException() {
        // Arrange
        List<Country> countries = List.of(new Country(1L, "Россия", "Москва"),
                new Country(2L, "Италия", "Рим"));
        Country newCountry = new Country();
        newCountry.setName("Италия");
        newCountry.setCapital("Рим");
        doReturn(Optional.of(countries.get(1))).when(countryRepository).findByName(newCountry.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> countryService.saveCountry(newCountry));

    }

    @Test
    void saveCountry_WhenNameExceedsMaxLength_ShouldThrowException() {
        // Arrange
        String tooLongName = "A".repeat(256);
        Country newCountry = new Country();
        newCountry.setName(tooLongName);
        newCountry.setCapital("Париж");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> countryService.saveCountry(newCountry),"Invalid country name");
    }

    @Test
    void saveCountry_WhenCapitalExceedsMaxLength_ShouldThrowException() {
        // Arrange
        String tooLongCapital = "A".repeat(129);
        Country newCountry = new Country();
        newCountry.setName("Франция");
        newCountry.setCapital(tooLongCapital);
        doReturn(Optional.empty()).when(countryRepository).findByName(newCountry.getName());

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> countryService.saveCountry(newCountry), "Invalid country capital");
    }

    @Test
    void deleteCountryById_WhenNoHotelsInCountry_ShouldDeleteCountry() {
        // Arrange
        long countryId = 1;
        Country country = new Country(countryId, "США", "Вашингтон");

        doReturn(Optional.of(country)).when(countryRepository).findById(countryId);
        doReturn(0).when(hotelService).getHotelCountByCountryId(countryId);

        // Act & Assert
        assertDoesNotThrow(() -> countryService.deleteCountryById(countryId));
        verify(this.countryRepository).delete(country);
    }

    @Test
    void deleteCountryById_WhenHotelsExistInCountry_ShouldThrowException() {
        // Arrange
        long countryId = 1;
        Country country = new Country(countryId, "США", "Вашингтон");

        doReturn(Optional.of(country)).when(countryRepository).findById(countryId);
        doReturn(1).when(hotelService).getHotelCountByCountryId(countryId);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> countryService.deleteCountryById(countryId),
                "Cannot delete country with existing hotels.");
    }

    @Test
    void deleteCountryById_WhenCountryNotFound_ShouldThrowException() {
        // Arrange
        long countryId = 1;

        doReturn(Optional.empty()).when(countryRepository).findById(countryId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> countryService.deleteCountryById(countryId));
    }
}