package com.example.travelportal.controllers;

import com.example.travelportal.dto.dto.HotelDto;
import com.example.travelportal.dto.dto.HotelDtoConverter;
import com.example.travelportal.model.Hotel;
import com.example.travelportal.services.hotel.HotelService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final HotelDtoConverter hotelDtoConverter;

    @Autowired
    public HotelController(@Qualifier("defaultHotelService") HotelService hotelService,
                           HotelDtoConverter hotelDtoConverter) {
        this.hotelService = hotelService;
        this.hotelDtoConverter = hotelDtoConverter;
    }

    @GetMapping("/")
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        try {
            List<HotelDto> hotelDtos = hotelService.getAllHotels().stream()
                    .map(hotelDtoConverter::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(hotelDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById (@PathVariable long id) {
        try {
            Hotel hotel = hotelService.getHotelById(id);
            if (hotel != null) {
                return new ResponseEntity<>(hotelDtoConverter.convertToDto(hotel), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable long id, @RequestBody HotelDto hotelDto) {
        try{
            Hotel hotel = hotelDtoConverter.convertToEntity(hotelDto);
            hotel.setId(id);
            Hotel updatedHotel = hotelService.updateHotel(hotel);
            return new ResponseEntity<>(hotelDtoConverter.convertToDto(updatedHotel), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/")
    public ResponseEntity<HotelDto> createHotel(@RequestBody HotelDto hotelDto) {
        return null;
    }
}
