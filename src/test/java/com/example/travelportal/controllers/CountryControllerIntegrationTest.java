package com.example.travelportal.controllers;

import com.example.travelportal.dto.country.CountryDto;
import com.example.travelportal.dto.country.CountryDtoConverter;
import com.example.travelportal.model.Country;
import com.example.travelportal.repositories.CountryRepository;
import com.example.travelportal.repositories.HotelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
class CountryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryDtoConverter countryDtoConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCountries_ShouldReturnListOfCountries() throws Exception {
        // Arrange
        List<Country> countries = List.of(
                Country.builder().name("TestCountry1").capital("TestCapital1").build(),
                Country.builder().name("TestCountry2").capital("TestCapital2").build(),
                Country.builder().name("TestCountry3").capital("TestCapital3").build(),
                Country.builder().name("TestCountry4").capital("TestCapital4").build()
        );
        countryRepository.saveAll(countries);
        List<CountryDto> countriesDto = countryRepository.findAll().stream()
                .map(countryDtoConverter::convertToDto)
                .toList();

        // Act
        mockMvc.perform(get("/api/countries/"))
                // Assert
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(countriesDto)));
    }


}