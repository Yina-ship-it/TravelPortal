package com.example.travelportal.services.country;

import com.example.travelportal.model.Country;
import com.example.travelportal.repositories.CountryRepository;
import com.example.travelportal.services.hotel.HotelService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Primary
public class DefaultCountryService implements CountryService {

    private final CountryRepository countryRepository;

    private final HotelService hotelService;

    @Autowired
    public DefaultCountryService(CountryRepository countryRepository,
                                 @Lazy HotelService hotelService) {
        this.countryRepository = countryRepository;
        this.hotelService = hotelService;
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
    @Transactional
    public Country updateCountry(Country country) {
        Optional<Country> oldCountry = countryRepository.findById(country.getId());
        if(oldCountry.isEmpty())
            throw new EntityNotFoundException("Country with id " + country.getId() + " not found");
        Optional<Country> countryDuplicatedName = countryRepository.findByName(country.getName());
        if (country.getName() == null ||
                country.getName().length() > 255 ||
                (countryDuplicatedName.isPresent() && !Objects.equals(countryDuplicatedName.get().getId(), country.getId())))
            throw new DataIntegrityViolationException("Invalid country name");
        if (country.getCapital() == null || country.getCapital().length() > 128)
            throw new DataIntegrityViolationException("Invalid country capital");

        return countryRepository.save(country);
    }

    @Override
    @Transactional
    public Country saveCountry(Country country) {
        if (country.getName() == null || country.getName().length() > 255 || countryRepository.findByName(country.getName()).isPresent())
            throw new DataIntegrityViolationException("Invalid country name");
        if (country.getCapital() == null || country.getCapital().length() > 128)
            throw new DataIntegrityViolationException("Invalid country capital");

        return countryRepository.save(country);
    }

    @Override
    @Transactional
    public void deleteCountryById(long countryId) {
        Country country = countryRepository.findById(countryId).orElseThrow(
                () -> new EntityNotFoundException("Country with id " + countryId + " not found"));
        if (hotelService.getHotelCountByCountryId(countryId) > 0)
            throw new IllegalArgumentException("Cannot delete country with existing hotels.");

        countryRepository.delete(country);
    }
}
