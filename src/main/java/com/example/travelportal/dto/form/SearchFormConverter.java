package com.example.travelportal.dto.form;

import com.example.travelportal.dto.DtoConverter;
import com.example.travelportal.model.Country;
import org.springframework.stereotype.Component;

@Component
public class SearchFormConverter implements DtoConverter<Country, SearchForm> {
    @Override
    public SearchForm convertToDto(Country country) {
        return new SearchForm("", country.toString());
    }

    @Override
    public Country convertToEntity(SearchForm searchForm) {
        try {
            String selectedCountry = searchForm.getSelectedCountry();

            long id = Long.parseLong((selectedCountry.split(", ")[0].split("=")[1]));
            String name = selectedCountry.split(", ")[1].split("=")[1];
            String capital = selectedCountry.split(", ")[2].split("=")[1].replace(")", "");

            return new Country(id,
                    getNonBlankString(name),
                    getNonBlankString(capital));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getNonBlankString(String value) {
        return (value != null && !value.isBlank()) ? value : null;
    }

}
