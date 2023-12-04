package com.example.travelportal.dto.form;

import com.example.travelportal.dto.DtoConverter;
import com.example.travelportal.model.Country;

public class SearchFormConverter implements DtoConverter<Country, SearchForm> {
    @Override
    public SearchForm convertToDto(Country country) {
        return null;
    }

    @Override
    public Country convertToEntity(SearchForm searchForm) {
        return null;
    }
}
