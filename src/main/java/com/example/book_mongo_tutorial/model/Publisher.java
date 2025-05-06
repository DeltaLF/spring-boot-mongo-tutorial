package com.example.book_mongo_tutorial.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "publishers")
public class Publisher {

    @Id
    private String id;

    private String name;

    private String address;

    /** List of references to {@link Person} IDs for signed writers. */
    private List<String> signedWriterIds;

    @Field("deleted_at")
    @Indexed
    private Instant deletedAt = null;
}