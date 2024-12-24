package com.ecommerce.project.security.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20, message = "Username must contain minimum 3 characters and maximum 20 characters!")
    private String username;

    @NotBlank
    @Size(max = 50, message = "Email must contain at least 3 characters!")
    @Email
    private String email;

    @Getter
    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}