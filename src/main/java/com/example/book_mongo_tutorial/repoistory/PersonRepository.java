package com.example.book_mongo_tutorial.repoistory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.book_mongo_tutorial.model.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

    /** Finds a non-deleted person by email. */
    Optional<Person> findByEmailAndDeletedAtIsNull(String email);

    /** Finds non-deleted people whose IDs are in the given collection. */
    List<Person> findByIdInAndDeletedAtIsNull(Collection<String> ids);

    /** Finds all non-deleted people. */
    List<Person> findByDeletedAtIsNull();

    /** Finds a non-deleted person by ID. */
    Optional<Person> findByIdAndDeletedAtIsNull(String id);

}