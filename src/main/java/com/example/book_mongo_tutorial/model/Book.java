package com.example.book_mongo_tutorial.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Note:
 * The responsibility of linking the models are on the service layer.
 * The comment is just to show the relationship between the models.
 * The alternative is to use @DBRef
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    @Indexed(unique = true)
    private String isbn;

    @Field("publication_date")
    private LocalDate publicationDate;

    /** Reference to {@link Publisher} ID */
    @Indexed
    private String publisherId;

    /** List of references to {@link Person} IDs for writers. Must not be empty. */
    private List<String> writerIds;

    /** List of references to {@link Person} IDs for readers. */
    private List<String> readerIds;

    @Field("deleted_at")
    @Indexed
    private Instant deletedAt = null;
}