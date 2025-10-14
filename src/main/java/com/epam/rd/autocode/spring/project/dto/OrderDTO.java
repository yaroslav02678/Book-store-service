package com.epam.rd.autocode.spring.project.dto;

import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;

    @Email(message = "Client email should be valid")
    @NotBlank(message = "Client email is required")
    private String clientEmail;

    @Email(message = "Employee email should be valid")
    @NotBlank(message = "Employee email is required")
    private String employeeEmail;

    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotEmpty(message = "Order must contain at least one book item")
    @Valid
    private List<BookItemDTO> bookItems;

    @NotNull(message = "Status should be valid")
    private OrderStatus status;
}
