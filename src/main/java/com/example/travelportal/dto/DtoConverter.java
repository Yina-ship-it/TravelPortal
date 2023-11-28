package com.example.travelportal.dto;

public interface DtoConverter<Entity, Dto> {
    Dto convertToDto(Entity entity);
    Entity convertToEntity(Dto dto);
}
