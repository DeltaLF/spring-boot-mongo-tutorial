package com.example.book_mongo_tutorial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDto {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "ISBN cannot be blank")
    // Consider adding @Pattern for ISBN format if needed
    private String isbn;

    @NotNull(message = "Publication date cannot be null")
    private LocalDate publicationDate;

    @NotBlank(message = "Publisher ID cannot be blank")
    private String publisherId;

    @NotEmpty(message = "Book must have at least one writer")
    private List<String> writerIds; // List of Person IDs

    private List<String> readerIds; // List of Person IDs (optional)
}