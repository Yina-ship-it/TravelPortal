package com.example.travelportal.dto.country;

import com.example.travelportal.dto.DtoConverter;
import com.example.travelportal.model.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryDtoConverter implements DtoConverter<Country, CountryDto> {
    @Override
    public CountryDto convertToDto(Country country) {
        return null;
    }

    @Override
    public Country convertToEntity(CountryDto countryDto) {
        return null;
    }
}
