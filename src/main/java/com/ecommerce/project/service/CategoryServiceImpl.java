package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        System.out.println("hello impl");
        Optional<Category> optionalCategory = categories.stream()
                .filter(c -> Objects.equals(c.getCategoryId(), categoryId))
                .findFirst();

        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!");
        }
    }

    @Override
    public String deleteCategory(Long categoryId){
        Category category = categories.stream()
                .filter(c -> Objects.equals(c.getCategoryId(), categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!"));

        if(category == null){
            return "Category with ID: " + categoryId + " is not found!!";
        }

        categories.remove(category);
        return "Category with categoryId: " + categoryId + " deleted successfully!";
    }
}
