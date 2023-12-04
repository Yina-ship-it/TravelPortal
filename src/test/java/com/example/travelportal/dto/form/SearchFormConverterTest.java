package com.example.travelportal.dto.form;

import com.example.travelportal.dto.country.CountryDto;
import com.example.travelportal.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchFormConverterTest {

    @InjectMocks
    SearchFormConverter converter;

    @Test
    void convertToDto_WhenCountryWithAllData_ShouldReturnSearchFormWithSearchInputBlankAndCountryString() {
        // Arrange
        Country country = new Country(1L, "TestCountry", "TestCapital");

        // Act
        SearchForm searchForm = converter.convertToDto(country);

        // Assert
        assertNotNull(searchForm);
        assertTrue(searchForm.getSearchInput().isBlank());
        assertEquals(country.toString(), searchForm.getSelectedCountry());
    }

    @Test
    void convertToEntity_WhenValidSearchForm_ShouldReturnCountry() {
        // Arrange
        Country inputCountry = new Country(1L, "TestCountry", "TestCapital");
        SearchForm searchForm = SearchForm.builder().selectedCountry(inputCountry.toString()).build();

        // Act
        Country country = converter.convertToEntity(searchForm);

        // Assert
        assertNotNull(country);
        assertEquals(inputCountry.getId(), country.getId());
        assertEquals(inputCountry.getName(), country.getName());
        assertEquals(inputCountry.getCapital(), country.getCapital());
    }

    @Test
    void convertToEntity_WhenSearchFormWithSelectedCountryWithoutId_ShouldReturnNull() {
        // Arrange
        Country inputCountry = Country.builder().name("TestCountry").capital("TestCapital").build();
        SearchForm searchForm = SearchForm.builder().selectedCountry(inputCountry.toString()).build();

        // Act
        Country country = converter.convertToEntity(searchForm);

        // Assert
        assertNull(country);
    }

    @Test
    void convertToEntity_WhenSearchFormWithSelectedCountryWithoutName_ShouldReturnNull() {
        // Arrange
        Country inputCountry = Country.builder().id(1L).capital("TestCapital").build();
        SearchForm searchForm = SearchForm.builder().selectedCountry(inputCountry.toString()).build();

        // Act
        Country country = converter.convertToEntity(searchForm);

        System.out.println(country.getName().isEmpty());

        // Assert
        assertNotNull(country);
        assertEquals(inputCountry.getId(), country.getId());
        assertEquals("null", country.getName());
        assertEquals(inputCountry.getCapital(), country.getCapital());
    }

    @Test
    void convertToEntity_WhenSearchFormWithSelectedCountryWithoutCapital_ShouldReturnNull() {
        // Arrange
        Country inputCountry = Country.builder().id(1L).name("TestCountry").build();
        SearchForm searchForm = SearchForm.builder().selectedCountry(inputCountry.toString()).build();

        // Act
        Country country = converter.convertToEntity(searchForm);

        // Assert
        assertNotNull(country);
        assertEquals(inputCountry.getId(), country.getId());
        assertEquals(inputCountry.getName(), country.getName());
        assertEquals("null", country.getCapital());
    }
}