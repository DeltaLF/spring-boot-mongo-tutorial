package com.example.book_mongo_tutorial.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.book_mongo_tutorial.dto.BookRequestDto;
import com.example.book_mongo_tutorial.dto.BookResponseDto;
import com.example.book_mongo_tutorial.exception.ResourceConflictException;
import com.example.book_mongo_tutorial.exception.ResourceNotFoundException;
import com.example.book_mongo_tutorial.exception.ValidationException;
import com.example.book_mongo_tutorial.mapper.BookMapper;
import com.example.book_mongo_tutorial.model.Book;
import com.example.book_mongo_tutorial.model.Person;
import com.example.book_mongo_tutorial.repoistory.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PersonService personService; // For validating writer/reader IDs
    private final PublisherService publisherService; // For validating publisher ID

    public BookResponseDto createBook(BookRequestDto requestDto) {
        bookRepository.findByIsbnAndDeletedAtIsNull(requestDto.getIsbn())
                .ifPresent(b -> {
                    throw new ResourceConflictException("Book with ISBN " + requestDto.getIsbn() + " already exists.");
                });

        // Validate publisher
        publisherService.findPublisherByIdInternal(requestDto.getPublisherId()); // Throws if not found/deleted

        // Validate writers
        if (requestDto.getWriterIds() == null || requestDto.getWriterIds().isEmpty()) {
            throw new ValidationException("Book must have at least one writer.");
        }
        for (String writerId : requestDto.getWriterIds()) {
            Person writer = personService.findPersonByIdInternal(writerId);
            if (!writer.getRoles().contains("WRITER")) {
                throw new ValidationException("Person with ID " + writerId + " is not a WRITER.");
            }
        }

        // Validate readers (if any)
        if (requestDto.getReaderIds() != null) {
            for (String readerId : requestDto.getReaderIds()) {
                Person reader = personService.findPersonByIdInternal(readerId);
                if (!reader.getRoles().contains("READER")) {
                    throw new ValidationException("Person with ID " + readerId + " is not a READER.");
                }
            }
        }

        Book book = bookMapper.toEntity(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    public List<BookResponseDto> getAllBooks() {
        return bookMapper.toDtoList(bookRepository.findByDeletedAtIsNull());
    }

    public BookResponseDto getBookById(String id) {
        Book book = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        return bookMapper.toDto(book);
    }

    public BookResponseDto updateBook(String id, BookRequestDto requestDto) {
        Book existingBook = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        if (!Objects.equals(existingBook.getIsbn(), requestDto.getIsbn())) {
            bookRepository.findByIsbnAndDeletedAtIsNull(requestDto.getIsbn())
                    .ifPresent(conflictingBook -> {
                        if (!Objects.equals(conflictingBook.getId(), existingBook.getId())) {
                            throw new ResourceConflictException(
                                    "ISBN " + requestDto.getIsbn() + " is already in use by another book.");
                        }
                    });
        }

        // Validate publisher, writers, readers as in createBook
        publisherService.findPublisherByIdInternal(requestDto.getPublisherId());

        if (requestDto.getWriterIds() == null || requestDto.getWriterIds().isEmpty()) {
            throw new ValidationException("Book must have at least one writer.");
        }
        for (String writerId : requestDto.getWriterIds()) {
            Person writer = personService.findPersonByIdInternal(writerId);
            if (!writer.getRoles().contains("WRITER")) {
                throw new ValidationException("Person with ID " + writerId + " is not a WRITER.");
            }
        }
        if (requestDto.getReaderIds() != null) {
            for (String readerId : requestDto.getReaderIds()) {
                Person reader = personService.findPersonByIdInternal(readerId);
                if (!reader.getRoles().contains("READER")) {
                    throw new ValidationException("Person with ID " + readerId + " is not a READER.");
                }
            }
        } else {
            requestDto.setReaderIds(new ArrayList<>()); // Assuming null means clear
        }

        bookMapper.updateEntityFromDto(requestDto, existingBook);
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(updatedBook);
    }

    public void deleteBook(String id) {
        Book book = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        book.setDeletedAt(Instant.now());
        bookRepository.save(book);
    }

    public BookResponseDto addReaderToBook(String bookId, String readerId) {
        Book book = bookRepository.findByIdAndDeletedAtIsNull(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));
        Person reader = personService.findPersonByIdInternal(readerId);
        if (!reader.getRoles().contains("READER")) {
            throw new ValidationException("Person with ID " + readerId + " is not a reader.");
        }
        if (book.getReaderIds() == null) {
            book.setReaderIds(new ArrayList<>());
        }
        if (!book.getReaderIds().contains(readerId)) {
            book.getReaderIds().add(readerId);
            bookRepository.save(book);
        }
        return bookMapper.toDto(book);
    }

    public BookResponseDto removeReaderFromBook(String bookId, String readerId) {
        Book book = bookRepository.findByIdAndDeletedAtIsNull(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));
        if (book.getReaderIds() != null && book.getReaderIds().contains(readerId)) {
            book.getReaderIds().remove(readerId);
            bookRepository.save(book);
        }
        return bookMapper.toDto(book);
    }

    public List<BookResponseDto> getBooksByPublisher(String publisherId) {
        publisherService.findPublisherByIdInternal(publisherId); // Validate publisher exists
        List<Book> books = bookRepository.findByPublisherIdAndDeletedAtIsNull(publisherId);
        return bookMapper.toDtoList(books);
    }

    public List<BookResponseDto> getBooksByWriter(String writerId) {
        personService.findPersonByIdInternal(writerId); // Validate writer exists
        List<Book> books = bookRepository.findByWriterIdsContainingAndDeletedAtIsNull(writerId);
        return bookMapper.toDtoList(books);
    }
}
