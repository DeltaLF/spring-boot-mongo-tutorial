package com.example.book_mongo_tutorial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.example.book_mongo_tutorial.dto.PublisherRequestDto;
import com.example.book_mongo_tutorial.dto.PublisherResponseDto;
import com.example.book_mongo_tutorial.model.Publisher;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    PublisherMapper INSTANCE = Mappers.getMapper(PublisherMapper.class);

    PublisherResponseDto toDto(Publisher publisher);

    Publisher toEntity(PublisherRequestDto publisherRequestDto);

    List<PublisherResponseDto> toDtoList(List<Publisher> publishers);

    void updateEntityFromDto(PublisherRequestDto dto, @MappingTarget Publisher entity);
}
