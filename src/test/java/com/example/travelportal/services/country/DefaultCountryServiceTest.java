package com.example.travelportal.services.country;

import com.example.travelportal.model.Country;
import com.example.travelportal.repositories.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultCountryServiceTest {
    @Mock
    CountryRepository countryRepository;

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
    void saveCountry() {
    }

    @Test
    void deleteCountryById() {
    }
}