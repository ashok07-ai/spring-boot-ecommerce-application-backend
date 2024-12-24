package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for the Category entity.
 * Extends JpaRepository to provide CRUD operations and custom query methods
 * for interacting with the Category table in the database.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a Category entity by its name.
     *
     * @param categoryName the name of the category to search for
     * @return the Category entity that matches the provided name, or null if no such entity exists
     */
    Category findByCategoryName(String categoryName);
}
