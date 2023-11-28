package com.example.travelportal.dto.dto;

import com.example.travelportal.dto.DtoConverter;
import com.example.travelportal.model.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelDtoConverter implements DtoConverter<Hotel, HotelDto> {
    @Override
    public HotelDto convertToDto(Hotel hotel) {
        return null;
    }

    @Override
    public Hotel convertToEntity(HotelDto hotelDto) {
        return null;
    }
}
