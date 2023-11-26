package com.example.travelportal.data;

import com.example.travelportal.model.Country;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.repositories.CountryRepository;
import com.example.travelportal.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {
    private final HotelRepository hotelRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public DataLoader(HotelRepository hotelRepository, CountryRepository countryRepository) {
        this.hotelRepository = hotelRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args){
        loadCountries();
        loadHotels();
    }

    private void loadCountries() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/data/countries.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        countryRepository.saveAll(
                reader.lines()
                        .skip(1)
                        .map(line -> {
                            String[] fields = line.split(",");
                            Country country = new Country();
                            country.setName(fields[0]);
                            country.setCapital(fields[1]);
                            return country;
                        })
                        .collect(Collectors.toList())
        );
    }

    private void loadHotels() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/data/hotels.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

        hotelRepository.saveAll(
                reader.lines()
                        .skip(1)
                        .map(line -> {
                            String[] fields = line.split(",");
                            Hotel hotel = new Hotel();
                            hotel.setName(fields[0]);
                            hotel.setCountry(countryRepository.findByName(fields[1]).orElse(null));
                            hotel.setStars(Integer.parseInt(fields[2]));
                            if (fields.length > 3) {
                                hotel.setWebsite(fields[3]);
                            }
                            return hotel;
                        })
                        .collect(Collectors.toList())
        );
    }
}

