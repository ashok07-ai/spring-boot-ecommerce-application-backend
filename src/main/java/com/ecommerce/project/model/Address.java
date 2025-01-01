package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters!")
    private String streetName;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters!")
    private String buildingName;

    @NotBlank
    @Size(min = 2, message = "City name must be at least 5 characters!")
    private String city;

    @NotBlank
    @Size(min = 3, message = "State name must be at least 5 characters!")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 5 characters!")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pin code must be at least 5 characters!")
    private String pinCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String streetName, String buildingName, String city, String state, String country, String pinCode) {
        this.streetName = streetName;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }
}
