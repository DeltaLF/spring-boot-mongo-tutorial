package com.example.book_mongo_tutorial.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Bookstore API with MongoDB (Tutorial)", version = "v1.0.0", description = "This API provides endpoints for managing books, authors (people), and publishers in a MongoDB backend, built as part of a tutorial.", contact = @Contact(), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
public class OpenApiConfig {
}
