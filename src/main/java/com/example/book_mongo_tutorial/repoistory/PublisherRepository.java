package com.example.book_mongo_tutorial.repoistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.book_mongo_tutorial.model.Publisher;

@Repository
public interface PublisherRepository extends MongoRepository<Publisher, String> {

    /** Finds all non-deleted publishers. */
    List<Publisher> findByDeletedAtIsNull();

    /** Finds a non-deleted publisher by ID. */
    Optional<Publisher> findByIdAndDeletedAtIsNull(String id);

    /** Finds a non-deleted publisher by name. */
    Optional<Publisher> findByNameAndDeletedAtIsNull(String name);

}