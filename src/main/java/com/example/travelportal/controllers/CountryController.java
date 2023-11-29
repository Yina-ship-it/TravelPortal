package com.example.travelportal.controllers;

import com.example.travelportal.dto.country.CountryDto;
import com.example.travelportal.dto.country.CountryDtoConverter;
import com.example.travelportal.model.Country;
import com.example.travelportal.services.country.CountryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryService countryService;
    private final CountryDtoConverter countryDtoConverter;

    @Autowired
    public CountryController(@Qualifier("defaultCountryService") CountryService countryService,
                             CountryDtoConverter countryDtoConverter) {
        this.countryService = countryService;
        this.countryDtoConverter = countryDtoConverter;
    }

    @GetMapping("/")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        try {
            List<CountryDto> countries = countryService.getAllCountries().stream()
                    .map(countryDtoConverter::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(countries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable long id) {
        try {
            Country country = countryService.getCountryById(id);
            if (country != null) {
                return new ResponseEntity<>(countryDtoConverter.convertToDto(country), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<CountryDto> createCountry(@RequestBody CountryDto countryDTO) {
        try{
            countryDTO.setId(null);
            Country createdCountry = countryService.saveCountry(countryDtoConverter.convertToEntity(countryDTO));
            return new ResponseEntity<>(countryDtoConverter.convertToDto(createdCountry), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable long id, @RequestBody CountryDto countryDTO) {
        try{
            Country country = countryDtoConverter.convertToEntity(countryDTO);
            country.setId(id);
            Country createdCountry = countryService.updateCountry(country);
            return new ResponseEntity<>(countryDtoConverter.convertToDto(createdCountry), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable long id) {
        try {
            countryService.deleteCountryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

