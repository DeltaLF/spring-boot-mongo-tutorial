package com.example.book_mongo_tutorial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.example.book_mongo_tutorial.dto.PersonRequestDto;
import com.example.book_mongo_tutorial.dto.PersonResponseDto;
import com.example.book_mongo_tutorial.model.Person;

import java.util.List;

@Mapper(componentModel = "spring") // Generates a Spring Bean implementation
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class); // Optional: for non-Spring usage

    PersonResponseDto toDto(Person person);

    Person toEntity(PersonRequestDto personRequestDto);

    List<PersonResponseDto> toDtoList(List<Person> people);

    /**
     * Updates an existing Person entity from a PersonRequestDto.
     *
     * @param dto    The source DTO with updated values.
     * @param entity The target entity to be updated (annotated
     *               with @MappingTarget).
     */
    void updateEntityFromDto(PersonRequestDto dto, @MappingTarget Person entity);
}
