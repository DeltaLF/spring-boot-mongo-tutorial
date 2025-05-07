package com.example.book_mongo_tutorial.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherResponseDto {
    private String id;
    private String name;
    private String address;
    private List<String> signedWriterIds;
}