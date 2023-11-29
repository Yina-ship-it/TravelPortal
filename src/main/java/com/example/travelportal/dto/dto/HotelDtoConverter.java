package com.example.travelportal.dto.dto;

import com.example.travelportal.dto.DtoConverter;
import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelDtoConverter implements DtoConverter<Hotel, HotelDto> {
    @Override
    public HotelDto convertToDto(Hotel hotel) {
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .countryId(hotel.getCountry() != null ? hotel.getCountry().getId() : null)
                .countryName(hotel.getCountry() != null ? hotel.getCountry().getName() : null)
                .stars(hotel.getStars())
                .website(hotel.getWebsite())
                .build();
    }

    @Override
    public Hotel convertToEntity(HotelDto hotelDto) {
        Country country = Country.builder()
                .id(hotelDto.getCountryId())
                .name(getNonBlankString(hotelDto.getCountryName())).build();
        return Hotel.builder()
                .id(hotelDto.getId())
                .name(getNonBlankString(hotelDto.getName()))
                .stars(hotelDto.getStars())
                .country(country)
                .website(getNonBlankString(hotelDto.getWebsite()))
                .build();
    }

    private String getNonBlankString(String value) {
        return (value != null && !value.isBlank()) ? value : null;
    }
}
