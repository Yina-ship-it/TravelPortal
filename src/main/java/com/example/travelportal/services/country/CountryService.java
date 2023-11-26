package com.example.travelportal.services.country;

import com.example.travelportal.model.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();
    Country getCountryById(long countryId);
    Country updateCountry(Country country);
    Country saveCountry(Country country);
    void deleteCountryById(long countryId);
}
