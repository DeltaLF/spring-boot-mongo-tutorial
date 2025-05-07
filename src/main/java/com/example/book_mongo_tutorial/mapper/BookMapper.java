package com.example.book_mongo_tutorial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.example.book_mongo_tutorial.dto.BookRequestDto;
import com.example.book_mongo_tutorial.dto.BookResponseDto;
import com.example.book_mongo_tutorial.model.Book;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookResponseDto toDto(Book book);

    Book toEntity(BookRequestDto bookRequestDto);

    List<BookResponseDto> toDtoList(List<Book> books);

    void updateEntityFromDto(BookRequestDto dto, @MappingTarget Book entity);
}