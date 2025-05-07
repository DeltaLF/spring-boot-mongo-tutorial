package com.example.book_mongo_tutorial.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.book_mongo_tutorial.dto.PublisherRequestDto;
import com.example.book_mongo_tutorial.dto.PublisherResponseDto;
import com.example.book_mongo_tutorial.exception.ResourceConflictException;
import com.example.book_mongo_tutorial.exception.ResourceNotFoundException;
import com.example.book_mongo_tutorial.exception.ValidationException;
import com.example.book_mongo_tutorial.mapper.PublisherMapper;
import com.example.book_mongo_tutorial.model.Person;
import com.example.book_mongo_tutorial.model.Publisher;
import com.example.book_mongo_tutorial.repoistory.PublisherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;
    private final PersonService personService; // For validating writer IDs

    public PublisherResponseDto createPublisher(PublisherRequestDto requestDto) {
        publisherRepository.findByNameAndDeletedAtIsNull(requestDto.getName())
                .ifPresent(p -> {
                    throw new ResourceConflictException(
                            "Publisher with name " + requestDto.getName() + " already exists.");
                });

        // Validate writer IDs
        if (requestDto.getSignedWriterIds() != null) {
            for (String writerId : requestDto.getSignedWriterIds()) {
                Person writer = personService.findPersonByIdInternal(writerId); // Throws if not found or deleted
                if (!writer.getRoles().contains("WRITER")) {
                    throw new ValidationException("Person with ID " + writerId + " is not a WRITER.");
                }
            }
        }

        Publisher publisher = publisherMapper.toEntity(requestDto);
        Publisher savedPublisher = publisherRepository.save(publisher);
        return publisherMapper.toDto(savedPublisher);
    }

    public List<PublisherResponseDto> getAllPublishers() {
        return publisherMapper.toDtoList(publisherRepository.findByDeletedAtIsNull());
    }

    public PublisherResponseDto getPublisherById(String id) {
        Publisher publisher = publisherRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with ID: " + id));
        return publisherMapper.toDto(publisher);
    }

    public PublisherResponseDto updatePublisher(String id, PublisherRequestDto requestDto) {
        Publisher existingPublisher = publisherRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with ID: " + id));

        if (!Objects.equals(existingPublisher.getName(), requestDto.getName())) {
            publisherRepository.findByNameAndDeletedAtIsNull(requestDto.getName())
                    .ifPresent(conflictingPublisher -> {
                        if (!Objects.equals(conflictingPublisher.getId(), existingPublisher.getId())) {
                            throw new ResourceConflictException(
                                    "Publisher name " + requestDto.getName() + " is already in use.");
                        }
                    });
        }

        // Validate writer IDs for update
        if (requestDto.getSignedWriterIds() != null) {
            for (String writerId : requestDto.getSignedWriterIds()) {
                Person writer = personService.findPersonByIdInternal(writerId);
                if (!writer.getRoles().contains("WRITER")) {
                    throw new ValidationException("Person with ID " + writerId + " is not a WRITER.");
                }
            }
        } else { // if null, maybe clear the list? Or treat as no change for this field?
            requestDto.setSignedWriterIds(new ArrayList<>()); // Assuming null means clear
        }

        publisherMapper.updateEntityFromDto(requestDto, existingPublisher);
        Publisher updatedPublisher = publisherRepository.save(existingPublisher);
        return publisherMapper.toDto(updatedPublisher);
    }

    public void deletePublisher(String id) {
        Publisher publisher = publisherRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with ID: " + id));
        publisher.setDeletedAt(Instant.now());
        publisherRepository.save(publisher);
    }

    public PublisherResponseDto addWriterToPublisher(String publisherId, String writerId) {
        Publisher publisher = publisherRepository.findByIdAndDeletedAtIsNull(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with ID: " + publisherId));
        Person writer = personService.findPersonByIdInternal(writerId);
        if (!writer.getRoles().contains("WRITER")) {
            throw new ValidationException("Person with ID " + writerId + " is not a writer.");
        }
        if (publisher.getSignedWriterIds() == null) {
            publisher.setSignedWriterIds(new ArrayList<>());
        }
        if (!publisher.getSignedWriterIds().contains(writerId)) {
            publisher.getSignedWriterIds().add(writerId);
            publisherRepository.save(publisher);
        }
        return publisherMapper.toDto(publisher);
    }

    public PublisherResponseDto removeWriterFromPublisher(String publisherId, String writerId) {
        Publisher publisher = publisherRepository.findByIdAndDeletedAtIsNull(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with ID: " + publisherId));
        // No need to validate writerId existence here, just remove if present
        if (publisher.getSignedWriterIds() != null && publisher.getSignedWriterIds().contains(writerId)) {
            publisher.getSignedWriterIds().remove(writerId);
            publisherRepository.save(publisher);
        }
        return publisherMapper.toDto(publisher);
    }

    // Helper for BookService
    protected Publisher findPublisherByIdInternal(String id) {
        return publisherRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Publisher (internal lookup) not found with ID: " + id));
    }
}