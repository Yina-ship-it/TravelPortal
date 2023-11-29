package com.example.travelportal.controllers;

import com.example.travelportal.dto.dto.HotelDto;
import com.example.travelportal.dto.dto.HotelDtoConverter;
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
}