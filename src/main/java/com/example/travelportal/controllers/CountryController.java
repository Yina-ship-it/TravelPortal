package com.example.travelportal.controllers;

import com.example.travelportal.services.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(@Qualifier("defaultCountryService") CountryService countryService) {
        this.countryService = countryService;
    }


}

