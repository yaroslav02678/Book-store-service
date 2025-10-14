package com.epam.rd.autocode.spring.project.model;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "age_group")
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(name = "price")
    private BigDecimal price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publication_year")
    private LocalDate publicationDate;

    @Column(name = "author")
    private String author;

    @Column(name = "number_of_pages")
    private Integer pages;

    @Column(name = "characteristics")
    private String characteristics;

    @Column(name = "description")
    private String description;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "image_url")
    private String imageUrl;
}
