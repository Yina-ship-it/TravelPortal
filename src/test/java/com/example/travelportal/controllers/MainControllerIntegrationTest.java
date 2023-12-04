package com.example.travelportal.controllers;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.repositories.CountryRepository;
import com.example.travelportal.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
class MainControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void testHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("searchForm"))
                .andExpect(model().attributeExists("countries"));
    }

    @Test
    void testSearch() throws Exception {
        Country country = countryRepository.save(
                Country.builder().name("TestCountry").capital("TestCapital").build());
        List<Hotel> hotels = new ArrayList<>(List.of(
                Hotel.builder().name("HotelTest1").country(country).stars(5).build(),
                Hotel.builder().name("HotelTest2").country(country).stars(4).build(),
                Hotel.builder().name("HotelTest3").country(country).stars(3).build(),
                Hotel.builder().name("HotelTest4").country(country).stars(2).build(),
                Hotel.builder().name("HotelTest5").country(country).stars(1).build()
        ));
        hotelRepository.saveAll(hotels);

        mockMvc.perform(get("/search")
                        .param("searchInput", "eltte")
                        .param("selectedCountry", country.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("searchResults"))
                .andExpect(model().attributeExists("searchResults"));
    }
}