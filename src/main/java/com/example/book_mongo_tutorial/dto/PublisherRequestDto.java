package com.example.book_mongo_tutorial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherRequestDto {

    @NotBlank(message = "Publisher name cannot be blank")
    @Size(min = 2, max = 100, message = "Publisher name must be between 2 and 100 characters")
    private String name;

    private String address; // Optional validation: @Size if needed

    private List<String> signedWriterIds; // List of Person IDs
}