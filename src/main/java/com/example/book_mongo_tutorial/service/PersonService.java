package com.example.book_mongo_tutorial.service;

import java.time.Instant; // Lombok for constructor injection
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.book_mongo_tutorial.dto.PersonRequestDto;
import com.example.book_mongo_tutorial.dto.PersonResponseDto;
import com.example.book_mongo_tutorial.exception.ResourceConflictException;
import com.example.book_mongo_tutorial.exception.ResourceNotFoundException;
import com.example.book_mongo_tutorial.mapper.PersonMapper;
import com.example.book_mongo_tutorial.model.Person;
import com.example.book_mongo_tutorial.repoistory.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Creates a constructor with all final fields (for dependency injection)
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonResponseDto createPerson(PersonRequestDto requestDto) {
        // Check if email already exists (for non-deleted users)
        personRepository.findByEmailAndDeletedAtIsNull(requestDto.getEmail())
                .ifPresent(existingPerson -> {
                    throw new ResourceConflictException(
                            "Person with email " + requestDto.getEmail() + " already exists.");
                });

        Person person = personMapper.toEntity(requestDto);
        Person savedPerson = personRepository.save(person);
        return personMapper.toDto(savedPerson);
    }

    public List<PersonResponseDto> getAllPeople() {
        List<Person> people = personRepository.findByDeletedAtIsNull();
        return personMapper.toDtoList(people);
    }

    public PersonResponseDto getPersonById(String id) {
        Person person = personRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));
        return personMapper.toDto(person);
    }

    public PersonResponseDto updatePerson(String id, PersonRequestDto requestDto) {
        Person existingPerson = personRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));

        // Check if the new email conflicts with another existing person
        if (!Objects.equals(existingPerson.getEmail(), requestDto.getEmail())) {
            personRepository.findByEmailAndDeletedAtIsNull(requestDto.getEmail())
                    .ifPresent(conflictingPerson -> {
                        if (!Objects.equals(conflictingPerson.getId(), existingPerson.getId())) {
                            throw new ResourceConflictException(
                                    "Email " + requestDto.getEmail() + " is already in use by another person.");
                        }
                    });
        }

        personMapper.updateEntityFromDto(requestDto, existingPerson);
        Person updatedPerson = personRepository.save(existingPerson);
        return personMapper.toDto(updatedPerson);
    }

    public void deletePerson(String id) { // Soft delete
        Person person = personRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));
        person.setDeletedAt(Instant.now());
        personRepository.save(person);
        // Note: We are not cleaning up references from Books/Publishers here.
        // This is consistent with our "handle at read time or through eventual cleanup"
        // strategy for soft delete.
    }

    // Helper method to be used by other services (BookService, PublisherService)
    public Person findPersonByIdInternal(String id) {
        return personRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person (internal lookup) not found with ID: " + id));
    }
}