package com.example.travelportal.controllers;

import com.example.travelportal.dto.form.SearchForm;
import com.example.travelportal.dto.form.SearchFormConverter;
import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.services.country.CountryService;
import com.example.travelportal.services.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MainController {

    final CountryService countryService;
    final HotelService hotelService;
    final SearchFormConverter searchFormConverter;

    @Autowired
    public MainController(CountryService countryService,
                          HotelService hotelService,
                          SearchFormConverter searchFormConverter) {
        this.countryService = countryService;
        this.hotelService = hotelService;
        this.searchFormConverter = searchFormConverter;
    }

    @GetMapping("/")
    public String home(Model model){
        SearchForm searchForm = new SearchForm();
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("countries", countryService.getAllCountries());
        return "index";
    }
}
