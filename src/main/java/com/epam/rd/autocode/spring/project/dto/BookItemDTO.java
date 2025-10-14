package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemDTO {

    @NotBlank(message = "Book name must not be blank")
    private String bookName;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
