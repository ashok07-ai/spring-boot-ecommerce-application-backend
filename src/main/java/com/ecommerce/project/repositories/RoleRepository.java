package com.ecommerce.project.repositories;

import com.ecommerce.project.enums.AppRole;
import com.ecommerce.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the Role entity.
 * Extends JpaRepository to provide CRUD operations and custom query methods
 * for interacting with the Role table in the database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a Role based on the provided AppRole.
     *
     * @param appRole the roleName to search for
     * @return an Optional containing the Role entity if found, or an empty Optional if not found
     */
    Optional<Role> findByRoleName(AppRole appRole);
}
