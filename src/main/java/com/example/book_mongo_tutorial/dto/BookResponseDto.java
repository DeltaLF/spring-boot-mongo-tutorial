package com.example.book_mongo_tutorial.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private String id;
    private String title;
    private String isbn;
    private LocalDate publicationDate;
    private String publisherId;
    private List<String> writerIds;
    private List<String> readerIds;
}