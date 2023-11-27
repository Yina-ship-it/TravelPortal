package com.example.travelportal.services.hotel;

import com.example.travelportal.model.Hotel;
import com.example.travelportal.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultHotelService implements HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public DefaultHotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }


    @Override
    public int getHotelCountByCountryId(long countryId) {
        return 0;
    }

    @Override
    @Transactional
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional
    public Hotel getHotelById(long hotelId) {
        return hotelRepository.findById(hotelId).orElse(null);
    }


    @Override
    public Hotel updateHotel(Hotel hotel) {
        return null;
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return null;
    }

    @Override
    public void deleteHotelById(long hotelId) {

    }
}
