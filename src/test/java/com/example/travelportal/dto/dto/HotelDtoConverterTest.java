package com.example.travelportal.dto.dto;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class HotelDtoConverterTest {

    @InjectMocks
    HotelDtoConverter converter;

    @Test
    void convertToDto_WhenHotelWithAllData_ShouldReturnCompleteHotelDto() {
        // Arrange
        Hotel hotel = new Hotel(1L,
                "TestHotel",
                new Country(1L, "TestCountry", "TestCapital"),
                5,
                "https://hotel.ru");

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertEquals(hotel.getCountry().getId(), hotelDto.getCountryId());
        assertEquals(hotel.getCountry().getName(), hotelDto.getCountryName());
        assertEquals(hotel.getStars(), hotelDto.getStars());
        assertEquals(hotel.getWebsite(), hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithoutId_ShouldReturnHotelDtoWithoutId() {
        // Arrange
        Hotel hotel = Hotel.builder()
                .name("TestHotel")
                .country(new Country(1L, "TestCountry", "TestCapital"))
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertNull(hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertEquals(hotel.getCountry().getId(), hotelDto.getCountryId());
        assertEquals(hotel.getCountry().getName(), hotelDto.getCountryName());
        assertEquals(hotel.getStars(), hotelDto.getStars());
        assertEquals(hotel.getWebsite(), hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithoutCountry_ShouldReturnHotelDtoWithoutCountryIdAndCountryName() {
        // Arrange
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("TestHotel")
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertNull(hotelDto.getCountryId());
        assertNull(hotelDto.getCountryName());
        assertEquals(hotel.getStars(), hotelDto.getStars());
        assertEquals(hotel.getWebsite(), hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithCountryWithoutId_ShouldReturnHotelDtoWithoutCountryId() {
        // Arrange
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("TestHotel")
                .country(Country.builder().name("TestCountry").capital("TestCapital").build())
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertNull(hotelDto.getCountryId());
        assertEquals(hotel.getCountry().getName(), hotelDto.getCountryName());
        assertEquals(hotel.getStars(), hotelDto.getStars());
        assertEquals(hotel.getWebsite(), hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithCountryWithoutName_ShouldReturnHotelDtoWithoutCountryName() {
        // Arrange
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("TestHotel")
                .country(Country.builder().id(1L).capital("TestCapital").build())
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertEquals(hotel.getCountry().getId(), hotelDto.getCountryId());
        assertNull(hotelDto.getCountryName());
        assertEquals(hotel.getStars(), hotelDto.getStars());
        assertEquals(hotel.getWebsite(), hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithoutName_ShouldReturnHotelDtoWithoutName() {
        // Arrange
        Hotel hotel = Hotel.builder()
                .id(1L)
                .country(new Country(1L, "TestCountry", "TestCapital"))
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertEquals(hotel.getId(), hotelDto.getId());
        assertNull(hotelDto.getName());
        assertEquals(hotel.getCountry().getId(), hotelDto.getCountryId());
        assertEquals(hotel.getCountry().getName(), hotelDto.getCountryName());
        assertEquals(hotel.getStars(), hotelDto.getStars());
        assertEquals(hotel.getWebsite(), hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithoutStars_ShouldReturnHotelDtoWithoutStars() {
        // Arrange
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("TestHotel")
                .country(new Country(1L, "TestCountry", "TestCapital"))
                .website("https://hotel.ru")
                .build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertEquals(hotel.getCountry().getId(), hotelDto.getCountryId());
        assertEquals(hotel.getCountry().getName(), hotelDto.getCountryName());
        assertNull(hotelDto.getStars());
        assertEquals(hotel.getWebsite(), hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithoutWebsite_ShouldReturnHotelDtoWithoutWebsite() {
        // Arrange
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("TestHotel")
                .country(new Country(1L, "TestCountry", "TestCapital"))
                .stars(5)
                .build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertEquals(hotel.getCountry().getId(), hotelDto.getCountryId());
        assertEquals(hotel.getCountry().getName(), hotelDto.getCountryName());
        assertEquals(hotel.getStars(), hotelDto.getStars());
        assertNull(hotelDto.getWebsite());
    }

    @Test
    void convertToDto_WhenHotelWithoutAllData_ShouldReturnHotelDtoWithoutAllData() {
        // Arrange
        Hotel hotel = Hotel.builder().build();

        // Act
        HotelDto hotelDto = converter.convertToDto(hotel);

        // Assert
        assertNotNull(hotelDto);
        assertNull(hotelDto.getId());
        assertNull(hotelDto.getName());
        assertNull(hotelDto.getCountryId());
        assertNull(hotelDto.getCountryName());
        assertNull(hotelDto.getStars());
        assertNull(hotelDto.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithAllData_ShouldReturnCompleteHotel() {
        // Arrange
        HotelDto hotelDto = new HotelDto(
                1L,
                "TestHotel",
                1L,
                "TestCountry",
                5,
                "https://hotel.ru");

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertEquals(hotelDto.getCountryName(), hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertEquals(hotelDto.getWebsite(), hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithoutId_ShouldReturnHotelWithoutId() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel")
                .countryId(1L)
                .countryName("TestCountry")
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertNull(hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertEquals(hotelDto.getCountryName(), hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertEquals(hotelDto.getWebsite(), hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithoutCountryId_ShouldReturnHotelWithCountryWithoutId() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("TestHotel")
                .countryName("TestCountry")
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertNull(hotel.getCountry().getId());
        assertEquals(hotelDto.getCountryName(), hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertEquals(hotelDto.getWebsite(), hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithoutCountryName_ShouldReturnHotelWithCountryWithoutName() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("TestHotel")
                .countryId(1L)
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertNull(hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertEquals(hotelDto.getWebsite(), hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithoutStars_ShouldReturnHotelWithoutStars() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("TestHotel")
                .countryId(1L)
                .countryName("TestCountry")
                .website("https://hotel.ru")
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertEquals(hotelDto.getCountryName(), hotel.getCountry().getName());
        assertNull(hotel.getStars());
        assertEquals(hotelDto.getWebsite(), hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithoutWebsite_ShouldReturnHotelWithoutWebsite() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("TestHotel")
                .countryId(1L)
                .countryName("TestCountry")
                .stars(5)
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertEquals(hotelDto.getCountryName(), hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertNull(hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithBlankName_ShouldReturnHotelWithoutName() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("")
                .countryId(1L)
                .countryName("TestCountry")
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertNull(hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertEquals(hotelDto.getCountryName(), hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertEquals(hotelDto.getWebsite(), hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithBlankCountryName_ShouldReturnHotelWithCountryWithoutName() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("TestHotel")
                .countryId(1L)
                .countryName("")
                .stars(5)
                .website("https://hotel.ru")
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertNull(hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertEquals(hotelDto.getWebsite(), hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithBlankWebsite_ShouldReturnHotelWithoutWebsite() {
        // Arrange
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("TestHotel")
                .countryId(1L)
                .countryName("TestCountry")
                .stars(5)
                .website("")
                .build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertNotNull(hotel.getCountry());
        assertEquals(hotelDto.getCountryId(), hotel.getCountry().getId());
        assertEquals(hotelDto.getCountryName(), hotel.getCountry().getName());
        assertEquals(hotelDto.getStars(), hotel.getStars());
        assertNull(hotel.getWebsite());
    }

    @Test
    void convertToEntity_WhenHotelDtoWithoutAllData_ShouldReturnHotelWithoutAllDataExceptCountryWithoutAllData(){
        // Arrange
        HotelDto hotelDto = HotelDto.builder().build();

        // Act
        Hotel hotel = converter.convertToEntity(hotelDto);

        // Assert
        assertNotNull(hotel);
        assertNull(hotel.getId());
        assertNull(hotel.getName());
        assertNotNull(hotel.getCountry());
        assertNull(hotel.getCountry().getId());
        assertNull(hotel.getCountry().getName());
        assertNull(hotel.getStars());
        assertNull(hotel.getWebsite());
    }
}