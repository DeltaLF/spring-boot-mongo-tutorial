package com.example.book_mongo_tutorial.repoistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.book_mongo_tutorial.model.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    /** Finds a non-deleted book by ISBN. */
    Optional<Book> findByIsbnAndDeletedAtIsNull(String isbn);

    /** Finds all non-deleted books by a specific publisher ID. */
    List<Book> findByPublisherIdAndDeletedAtIsNull(String publisherId);

    /** Finds all non-deleted books containing a specific writer ID. */
    List<Book> findByWriterIdsContainingAndDeletedAtIsNull(String writerId);

    /** Finds all non-deleted books containing a specific reader ID. */
    List<Book> findByReaderIdsContainingAndDeletedAtIsNull(String readerId);

    /** Finds all non-deleted books. */
    List<Book> findByDeletedAtIsNull();

    /** Finds a non-deleted book by ID. */
    Optional<Book> findByIdAndDeletedAtIsNull(String id);

}