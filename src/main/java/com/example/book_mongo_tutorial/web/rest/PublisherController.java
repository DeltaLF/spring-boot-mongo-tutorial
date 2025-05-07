package com.example.book_mongo_tutorial.web.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.book_mongo_tutorial.dto.BookResponseDto;
import com.example.book_mongo_tutorial.dto.PublisherRequestDto;
import com.example.book_mongo_tutorial.dto.PublisherResponseDto;
import com.example.book_mongo_tutorial.service.BookService;
import com.example.book_mongo_tutorial.service.PublisherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;
    private final BookService bookService; // If you want /publishers/{id}/books endpoint here

    @PostMapping
    public ResponseEntity<PublisherResponseDto> createPublisher(@Valid @RequestBody PublisherRequestDto requestDto) {
        PublisherResponseDto createdPublisher = publisherService.createPublisher(requestDto);
        return new ResponseEntity<>(createdPublisher, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponseDto>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> getPublisherById(@PathVariable String id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> updatePublisher(@PathVariable String id,
            @Valid @RequestBody PublisherRequestDto requestDto) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable String id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{publisherId}/writers/{writerId}")
    public ResponseEntity<PublisherResponseDto> addWriterToPublisher(@PathVariable String publisherId,
            @PathVariable String writerId) {
        PublisherResponseDto updatedPublisher = publisherService.addWriterToPublisher(publisherId, writerId);
        return ResponseEntity.ok(updatedPublisher);
    }

    @DeleteMapping("/{publisherId}/writers/{writerId}")
    public ResponseEntity<PublisherResponseDto> removeWriterFromPublisher(@PathVariable String publisherId,
            @PathVariable String writerId) {
        PublisherResponseDto updatedPublisher = publisherService.removeWriterFromPublisher(publisherId, writerId);
        return ResponseEntity.ok(updatedPublisher);
    }

    @GetMapping("/{publisherId}/books")
    public ResponseEntity<List<BookResponseDto>> getBooksByPublisher(@PathVariable String publisherId) {
        List<BookResponseDto> books = bookService.getBooksByPublisher(publisherId);
        return ResponseEntity.ok(books);
    }
}