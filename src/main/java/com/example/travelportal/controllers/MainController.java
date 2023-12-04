package com.example.travelportal.controllers;

import com.example.travelportal.services.country.CountryService;
import com.example.travelportal.services.hotel.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    final CountryService countryService;
    final HotelService hotelService;

    @Autowired
    public MainController(CountryService countryService, HotelService hotelService) {
        this.countryService = countryService;
        this.hotelService = hotelService;
    }

    @GetMapping("/")
    public String home(Model model){
        return null;
    }
}
