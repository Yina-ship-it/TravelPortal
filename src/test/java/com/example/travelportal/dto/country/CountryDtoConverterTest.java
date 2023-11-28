package com.example.travelportal.dto.country;

import com.example.travelportal.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CountryDtoConverterTest {

    @InjectMocks
    CountryDtoConverter converter;

    @Test
    void convertToDto_WhenCountryWithAllData_ShouldReturnCompleteCountryDto() {
        // Arrange
        Country country = new Country(1L, "TestCountry", "TestCapital");

        // Act
        CountryDto countryDto = converter.convertToDto(country);

        // Assert
        assertNotNull(countryDto);
        assertEquals(country.getId(), countryDto.getId());
        assertEquals(country.getName(), countryDto.getName());
        assertEquals(country.getCapital(), countryDto.getCapital());
    }

    @Test
    void convertToDto_WhenCountryWithoutId_ShouldReturnCountryDtoWithoutId() {
        // Arrange
        Country country = Country.builder().name("TestCountry").capital("TestCapital").build();

        // Act
        CountryDto countryDto = converter.convertToDto(country);

        // Assert
        assertNotNull(countryDto);
        assertNull(countryDto.getId());
        assertEquals(country.getName(), countryDto.getName());
        assertEquals(country.getCapital(), countryDto.getCapital());
    }

    @Test
    void convertToDto_WhenCountryWithoutName_ShouldReturnCountryDtoWithoutName() {
        // Arrange
        Country country = Country.builder().id(1L).capital("TestCapital").build();

        // Act
        CountryDto countryDto = converter.convertToDto(country);

        // Assert
        assertNotNull(countryDto);
        assertEquals(country.getId(), countryDto.getId());
        assertNull(countryDto.getName());
        assertEquals(country.getCapital(), countryDto.getCapital());
    }

    @Test
    void convertToDto_WhenCountryWithoutCapital_ShouldReturnCountryDtoWithoutCapital() {
        // Arrange
        Country country = Country.builder().id(1L).name("TestCountry").build();

        // Act
        CountryDto countryDto = converter.convertToDto(country);

        // Assert
        assertNotNull(countryDto);
        assertEquals(country.getId(), countryDto.getId());
        assertEquals(country.getName(), countryDto.getName());
        assertNull(countryDto.getCapital());
    }

    @Test
    void convertToDto_WhenCountryWithoutAllData_ShouldReturnCountryDtoWithoutAllData() {
        // Arrange
        Country country = Country.builder().build();

        // Act
        CountryDto countryDto = converter.convertToDto(country);

        // Assert
        assertNotNull(countryDto);
        assertNull(countryDto.getId());
        assertNull(countryDto.getName());
        assertNull(countryDto.getCapital());
    }

    @Test
    void convertToEntity_WhenCountryDtoWithAllData_ShouldReturnCompleteCountry() {
        // Arrange
        CountryDto countryDto = new CountryDto(1L, "TestCountry", "TestCapital");

        // Act
        Country country = converter.convertToEntity(countryDto);

        // Assert
        assertNotNull(country);
        assertEquals(countryDto.getId(), country.getId());
        assertEquals(countryDto.getName(), country.getName());
        assertEquals(countryDto.getCapital(), country.getCapital());
    }

    @Test
    void convertToEntity_WhenCountryDtoWithoutId_ShouldReturnCountryWithoutId() {
        // Arrange
        CountryDto countryDto = CountryDto.builder().name("TestCountry").capital("TestCapital").build();

        // Act
        Country country = converter.convertToEntity(countryDto);

        // Assert
        assertNotNull(country);
        assertNull(country.getId());
        assertEquals(countryDto.getName(), country.getName());
        assertEquals(countryDto.getCapital(), country.getCapital());
    }

    @Test
    void convertToEntity_WhenCountryDtoWithoutName_ShouldReturnCountryWithoutName() {
        // Arrange
        CountryDto countryDto = CountryDto.builder().id(1L).capital("TestCapital").build();

        // Act
        Country country = converter.convertToEntity(countryDto);

        // Assert
        assertNotNull(country);
        assertEquals(countryDto.getId(), country.getId());
        assertNull(country.getName());
        assertEquals(countryDto.getCapital(), country.getCapital());
    }

    @Test
    void convertToEntity_WhenCountryDtoWithoutCapital_ShouldReturnCountryWithoutCapital() {
        // Arrange
        CountryDto countryDto = CountryDto.builder().id(1L).name("TestCountry").build();

        // Act
        Country country = converter.convertToEntity(countryDto);

        // Assert
        assertNotNull(country);
        assertEquals(countryDto.getId(), country.getId());
        assertEquals(countryDto.getName(), country.getName());
        assertNull(country.getCapital());
    }

    @Test
    void convertToEntity_WhenCountryDtoWithoutAllData_ShouldReturnCountryWithoutAllData() {
        // Arrange
        CountryDto countryDto = CountryDto.builder().build();

        // Act
        Country country = converter.convertToEntity(countryDto);

        // Assert
        assertNotNull(country);
        assertNull(country.getId());
        assertNull(country.getName());
        assertNull(country.getCapital());
    }
}