package com.ecommerce.project.repositories;

import com.ecommerce.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the User entity.
 * Extends JpaRepository to provide CRUD operations and custom query methods
 * for interacting with the User table in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User entity by its username.
     *
     * @param username the username of the user to search for
     * @return an Optional containing the User entity if found, or empty if no user with the given username exists
     */
    Optional<User> findByUserName(String username);

    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    Boolean existsByUserName(String username);

    /**
     * Checks if a user with the given email exists in the database.
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    Boolean existsByEmail(String email);

}
