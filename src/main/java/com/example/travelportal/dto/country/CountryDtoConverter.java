package com.example.travelportal.dto.country;

import com.example.travelportal.dto.DtoConverter;
import com.example.travelportal.model.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryDtoConverter implements DtoConverter<Country, CountryDto> {
    @Override
    public CountryDto convertToDto(Country country) {
        return CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .capital(country.getCapital())
                .build();
    }

    @Override
    public Country convertToEntity(CountryDto countryDto) {
        return Country.builder()
                .id(countryDto.getId())
                .name(getNonBlankString(countryDto.getName()))
                .capital(getNonBlankString(countryDto.getCapital()))
                .build();
    }

    private String getNonBlankString(String value) {
        return (value != null && !value.isBlank()) ? value : null;
    }
}
