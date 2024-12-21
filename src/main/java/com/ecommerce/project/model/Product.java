package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Product name is required!")
    @Size(min = 3, message = "Product name must contain at least 3 characters!")
    private String productName;

    @NotBlank(message = "Image is required!")
    private String image;

    @NotBlank(message = "Description is required!")
    @Size(min = 6, message = "Product description must contain at least 6 characters!")
    private String description;
    private Integer quantity;

    private double price;
    private double discount;
    private double netPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
