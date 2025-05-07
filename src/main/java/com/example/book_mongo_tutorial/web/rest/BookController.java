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

import com.example.book_mongo_tutorial.dto.BookRequestDto;
import com.example.book_mongo_tutorial.dto.BookResponseDto;
import com.example.book_mongo_tutorial.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto requestDto) {
        BookResponseDto createdBook = bookService.createBook(requestDto);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable String id,
            @Valid @RequestBody BookRequestDto requestDto) {
        return ResponseEntity.ok(bookService.updateBook(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookId}/readers/{readerId}")
    public ResponseEntity<BookResponseDto> addReaderToBook(@PathVariable String bookId, @PathVariable String readerId) {
        BookResponseDto updatedBook = bookService.addReaderToBook(bookId, readerId);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}/readers/{readerId}")
    public ResponseEntity<BookResponseDto> removeReaderFromBook(@PathVariable String bookId,
            @PathVariable String readerId) {
        BookResponseDto updatedBook = bookService.removeReaderFromBook(bookId, readerId);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/by-writer/{writerId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByWriter(@PathVariable String writerId) {
        List<BookResponseDto> books = bookService.getBooksByWriter(writerId);
        return ResponseEntity.ok(books);
    }
}