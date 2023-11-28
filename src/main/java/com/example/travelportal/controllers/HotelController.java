package com.example.travelportal.controllers;

import com.example.travelportal.services.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;

    @Autowired
    public HotelController(@Qualifier("defaultHotelService") HotelService hotelService) {
        this.hotelService = hotelService;
    }


}
