package com.example.travelportal.services.hotel;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.repositories.HotelRepository;
import com.example.travelportal.services.country.CountryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Primary
public class DefaultHotelService implements HotelService {

    private final HotelRepository hotelRepository;
    private final CountryService countryService;

    @Autowired
    public DefaultHotelService(HotelRepository hotelRepository,
                               @Lazy CountryService countryService) {
        this.hotelRepository = hotelRepository;
        this.countryService = countryService;
    }


    @Override
    @Transactional
    public int getHotelCountByCountryId(long countryId) {
        return hotelRepository.countByCountry_Id(countryId);
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
    @Transactional
    public Hotel updateHotel(Hotel hotel) {
        Optional<Hotel> oldHotel = hotelRepository.findById(hotel.getId());
        if(oldHotel.isEmpty())
            throw new EntityNotFoundException("Hotel with id " + hotel.getId() + " not found");
        if(hotel.getCountry() == null || hotel.getCountry().getId() == null)
            throw new EntityNotFoundException("Country not found.");
        Country country = countryService.getCountryById(hotel.getCountry().getId());
        if (country == null)
            throw new EntityNotFoundException("Country with id " + hotel.getCountry().getId() +" not found.");
        Optional<Hotel> hotelWithDuplicatedName = hotelRepository.findByCountry_IdAndName(
                hotel.getCountry().getId(),
                hotel.getName());
        if (hotelWithDuplicatedName.isPresent() && !Objects.equals(hotelWithDuplicatedName.get().getId(), hotel.getId()))
            throw new DataIntegrityViolationException("Hotel with the name '" + hotel.getName() + "' already exists in the country.");
        validateHotelData(hotel);
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public Hotel saveHotel(Hotel hotel) {
        if(hotel.getCountry() == null || hotel.getCountry().getId() == null)
            throw new EntityNotFoundException("Country not found.");
        Country country = countryService.getCountryById(hotel.getCountry().getId());
        if (country == null)
            throw new EntityNotFoundException("Country with id " + hotel.getCountry().getId() +" not found.");
        hotel.setCountry(country);
        if (hotelRepository.findByCountry_IdAndName(hotel.getCountry().getId(), hotel.getName()).isPresent())
            throw new DataIntegrityViolationException("Hotel with the name '" + hotel.getName() + "' already exists in the country.");
        validateHotelData(hotel);
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public void deleteHotelById(long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () -> new EntityNotFoundException("Hotel with id " + hotelId + " not found."));

        hotelRepository.delete(hotel);
    }


    private void validateHotelData(Hotel hotel) {
        if (hotel.getName() == null || hotel.getName().length() > 255)
            throw new DataIntegrityViolationException("Invalid hotel name");
        if (hotel.getStars() == null || hotel.getStars() < 1 || hotel.getStars() > 5)
            throw new DataIntegrityViolationException("Invalid hotel stars");
        if (hotel.getWebsite() != null && !isValidWebsite(hotel.getWebsite()))
            throw new DataIntegrityViolationException("Invalid hotel website");
    }

    private boolean isValidWebsite(String website) {
        try {
            new URL(website);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
