package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for the Product entity.
 * Extends JpaRepository to provide CRUD operations and custom query methods
 * for interacting with the Product table in the database.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a list of Product entities associated with a specific Category.
     *
     * @param category the Category entity used to filter the products
     * @return a list of Product entities that belong to the specified category
     */
    List<Product> findByCategory(Category category);

    /**
     * Finds a paginated list of Product entities associated with a specific Category,
     * ordered by price in ascending order.
     *
     * @param category    the Category entity used to filter the products
     * @param pageDetails the pagination and sorting information
     * @return a paginated list of Product entities sorted by price in ascending order
     */
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    /**
     * Finds a paginated list of Product entities where the product name contains a
     * specific keyword, ignoring case.
     *
     * @param keyword     the keyword to search for in the product name
     * @param pageDetails the pagination and sorting information
     * @return a paginated list of Product entities that match the keyword
     */
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
