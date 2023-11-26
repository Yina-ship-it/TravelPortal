package com.example.travelportal.services.country;

import com.example.travelportal.model.Country;
import com.example.travelportal.repositories.CountryRepository;
import com.example.travelportal.services.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
public class DefaultCountryService implements CountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public DefaultCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    @Transactional
    public Country getCountryById(long countryId) {
        return countryRepository.findById(countryId).orElse(null);
    }

    @Override
    public Country saveCountry(Country country) {
        return null;
    }

    @Override
    public void deleteCountryById(long countryId) {

    }
}
