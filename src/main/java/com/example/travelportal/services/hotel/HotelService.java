package com.example.travelportal.services.hotel;

import com.example.travelportal.model.Hotel;

import java.util.List;

public interface HotelService {
    int getHotelCountByCountryId(long countryId);
    List<Hotel> getAllHotels();
    Hotel getHotelById(long hotelId);
    Hotel updateHotel(Hotel hotel);
    Hotel saveHotel(Hotel hotel);
    void deleteHotelById(long hotelId);
}
