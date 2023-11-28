package com.example.travelportal.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    private Long id;
    private String name;
    private Long countryId;
    private String countryName;
    private Integer stars;
    private String website;
}
