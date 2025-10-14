package com.epam.rd.autocode.spring.project.dto;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotBlank(message = "Book name must not be blank")
    private String name;

    @NotBlank(message = "Genre must not be blank")
    private String genre;

    @NotNull(message = "Age group must be selected")
    private AgeGroup ageGroup;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Publication date must not be null")
    @PastOrPresent(message = "Publication date cannot be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotNull(message = "Pages must not be null")
    @Positive(message = "Pages must be positive")
    private Integer pages;

    @Size(max = 500, message = "Characteristics too long (max 500 characters)")
    @Size(min = 10, message = "Characteristics must me > 10 characters")
    private String characteristics;

    @Size(max = 1000, message = "Description too long (max 1000 characters)")
    @Size(min = 10, message = "Description must me > 10 characters")
    private String description;

    @NotNull(message = "Language must be selected")
    private Language language;

    @NotBlank(message = "URL must not be blank")
    private String imageUrl;
}
