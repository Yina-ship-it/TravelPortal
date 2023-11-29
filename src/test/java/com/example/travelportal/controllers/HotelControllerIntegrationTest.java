package com.example.travelportal.controllers;

import com.example.travelportal.dto.hotel.HotelDto;
import com.example.travelportal.dto.hotel.HotelDtoConverter;
import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.repositories.CountryRepository;
import com.example.travelportal.repositories.HotelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private HotelDtoConverter hotelDtoConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllHotels_ShouldReturnListOfHotels() throws Exception {
        // Arrange
        Country country = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        List<Hotel> hotels = List.of(
                Hotel.builder().name("TestHotel1").country(country).stars(5).build(),
                Hotel.builder().name("TestHotel2").country(country).stars(5).build(),
                Hotel.builder().name("TestHotel3").country(country).stars(5).build(),
                Hotel.builder().name("TestHotel4").country(country).stars(5).build()
        );
        hotelRepository.saveAll(hotels);
        List<HotelDto> hotelDtos = hotelRepository.findAll().stream()
                .map(hotelDtoConverter::convertToDto)
                .toList();

        // Act
        mockMvc.perform(get("/api/hotels/"))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(hotelDtos)));
    }

    @Test
    void getHotelById_WhenHotelFound_ShouldReturnOkStatusAndHotelDto() throws Exception {
        // Arrange
        Country country = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel hotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country).stars(5).build());
        HotelDto hotelDto = hotelDtoConverter.convertToDto(hotel);

        // Act
        mockMvc.perform(get("/api/hotels/{id}", hotel.getId()))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(hotelDto)));
    }

    @Test
    void getHotelById_WhenHotelNotFound_ShouldReturnNotFoundStatus() throws Exception {
        // Arrange
        long id = Long.MAX_VALUE;

        // Act
        mockMvc.perform(get("/api/hotels/{id}", id))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateHotel_WhenHotelDtoWithValidData_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryName(country2.getName())
                .countryId(country2.getId())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        String responseContent  = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(hotelDto.getName(), updatedHotel.get().getName());
        assertEquals(hotelDto.getCountryId(), updatedHotel.get().getCountry().getId());
        assertEquals(hotelDto.getCountryName(), updatedHotel.get().getCountry().getName());
        assertEquals(hotelDto.getStars(), updatedHotel.get().getStars());
        assertEquals(hotelDto.getWebsite(), updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenHotelNotFound_ShouldReturnNotFoundStatus() throws Exception {
        // Arrange
        long id = Long.MAX_VALUE;
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryName(country2.getName())
                .countryId(country2.getId())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        mockMvc.perform(put("/api/hotels/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateHotel_WhenHotelDtoWithoutName_ShouldReturnBadRequestStatus() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .countryName(country2.getName())
                .countryId(country2.getId())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHotel_WhenHotelDtoWithBlankName_ShouldReturnBadRequestStatus() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("")
                .countryName(country2.getName())
                .countryId(country2.getId())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHotel_WhenHotelDtoWithoutCountryId_ShouldReturnNotFoundStatus() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryName(country2.getName())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateHotel_WhenHotelDtoWithCountryIdFromNonExistentCountry_ShouldReturnNotFoundStatus() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        long countryId = Long.MAX_VALUE;
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryId(countryId)
                .countryName(country1.getName())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateHotel_WhenHotelDtoWithoutCountryName_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryId(country2.getId())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(hotelDto.getName(), updatedHotel.get().getName());
        assertEquals(hotelDto.getCountryId(), updatedHotel.get().getCountry().getId());
        assertEquals(country2.getName(), updatedHotel.get().getCountry().getName());
        assertEquals(hotelDto.getStars(), updatedHotel.get().getStars());
        assertEquals(hotelDto.getWebsite(), updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenHotelDtoWithBlankCountryName_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryId(country2.getId())
                .countryName("")
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(hotelDto.getName(), updatedHotel.get().getName());
        assertEquals(hotelDto.getCountryId(), updatedHotel.get().getCountry().getId());
        assertEquals(country2.getName(), updatedHotel.get().getCountry().getName());
        assertEquals(hotelDto.getStars(), updatedHotel.get().getStars());
        assertEquals(hotelDto.getWebsite(), updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenHotelDtoWithoutStars_ShouldReturnBadRequestStatus() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryId(country2.getId())
                .countryName(country2.getName())
                .website("https://hotel2.ru").build();

        // Act
        mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHotel_WhenHotelDtoWithoutWebsite_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryId(country2.getId())
                .countryName(country2.getName())
                .stars(5).build();

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(hotelDto.getName(), updatedHotel.get().getName());
        assertEquals(hotelDto.getCountryId(), updatedHotel.get().getCountry().getId());
        assertEquals(hotelDto.getCountryName(), updatedHotel.get().getCountry().getName());
        assertEquals(oldHotel.getStars(), updatedHotel.get().getStars());
        assertNull(updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenHotelDtoWithBlankWebsite_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .name("TestHotel2")
                .countryId(country2.getId())
                .countryName(country2.getName())
                .stars(5)
                .website("").build();

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(hotelDto.getName(), updatedHotel.get().getName());
        assertEquals(hotelDto.getCountryId(), updatedHotel.get().getCountry().getId());
        assertEquals(hotelDto.getCountryName(), updatedHotel.get().getCountry().getName());
        assertEquals(oldHotel.getStars(), updatedHotel.get().getStars());
        assertNull(updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenHotelDtoWithValidDataAndId_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = HotelDto.builder()
                .id(1L)
                .name("TestHotel2")
                .countryName(country2.getName())
                .countryId(country2.getId())
                .stars(5)
                .website("https://hotel2.ru").build();

        // Act
        String responseContent  = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(hotelDto.getName(), updatedHotel.get().getName());
        assertEquals(hotelDto.getCountryId(), updatedHotel.get().getCountry().getId());
        assertEquals(hotelDto.getCountryName(), updatedHotel.get().getCountry().getName());
        assertEquals(hotelDto.getStars(), updatedHotel.get().getStars());
        assertEquals(hotelDto.getWebsite(), updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenOnlyNameUpdated_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        HotelDto hotelDto = hotelDtoConverter.convertToDto(oldHotel);
        hotelDto.setName("TestHotel2");

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(hotelDto.getName(), updatedHotel.get().getName());
        assertEquals(oldHotel.getCountry(), updatedHotel.get().getCountry());
        assertEquals(oldHotel.getStars(), updatedHotel.get().getStars());
        assertEquals(oldHotel.getWebsite(), updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenOnlyCountryUpdated_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        Country country2 = countryRepository.save(
                Country.builder().name("TestCountry2").capital("TestCapital2").build());
        HotelDto hotelDto = hotelDtoConverter.convertToDto(oldHotel);
        hotelDto.setCountryId(country2.getId());
        hotelDto.setCountryName(country2.getName());

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(oldHotel.getName(), updatedHotel.get().getName());
        assertEquals(country2, updatedHotel.get().getCountry());
        assertEquals(oldHotel.getStars(), updatedHotel.get().getStars());
        assertEquals(oldHotel.getWebsite(), updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenOnlyStarsUpdated_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        HotelDto hotelDto = hotelDtoConverter.convertToDto(oldHotel);
        hotelDto.setStars(5);

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(oldHotel.getName(), updatedHotel.get().getName());
        assertEquals(oldHotel.getCountry(), updatedHotel.get().getCountry());
        assertEquals(hotelDto.getStars(), updatedHotel.get().getStars());
        assertEquals(oldHotel.getWebsite(), updatedHotel.get().getWebsite());
    }

    @Test
    void updateHotel_WhenOnlyWebsiteUpdated_ShouldReturnCreatedStatusAndHotelDto() throws Exception {
        // Arrange
        Country country1 = countryRepository.save(
                Country.builder().name("TestCountry1").capital("TestCapital1").build());
        Hotel oldHotel = hotelRepository.save(
                Hotel.builder().name("TestHotel1").country(country1).stars(3).website("http://hotel1.ru").build());
        HotelDto hotelDto = hotelDtoConverter.convertToDto(oldHotel);
        hotelDto.setWebsite("https://hotel2.ru");

        // Act
        String responseContent = mockMvc.perform(put("/api/hotels/{id}", oldHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        //Assert
        HotelDto response = objectMapper.readValue(responseContent, HotelDto.class);
        Optional<Hotel> updatedHotel = hotelRepository.findById(response.getId());

        assertTrue(updatedHotel.isPresent());
        assertEquals(oldHotel.getId(), updatedHotel.get().getId());
        assertEquals(oldHotel.getName(), updatedHotel.get().getName());
        assertEquals(oldHotel.getCountry(), updatedHotel.get().getCountry());
        assertEquals(oldHotel.getStars(), updatedHotel.get().getStars());
        assertEquals(hotelDto.getWebsite(), updatedHotel.get().getWebsite());
    }
}