package com.example.blogjdbcnew.services;

import com.example.blogjdbcnew.entities.Category;
import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.repositories.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;


    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategoryById( Integer catid) {
        return categoryRepo.findById(catid);
    }


    public void createCategory(Category category) {
        validatePost(category);
        categoryRepo.save(category);
    }

    public void updateCategory(Integer catid,  Category updatedCategory) {
        Category existingCategory = categoryRepo.findById(catid);
        validatePost(existingCategory);
        if (existingCategory != null) {
            existingCategory.setTitle(updatedCategory.getTitle());
            existingCategory.setDescription(updatedCategory.getDescription());
            categoryRepo.update(existingCategory);
        }
    }

    public void deleteBlog(Integer catid) {
        categoryRepo.delete(catid);
    }

    private void validatePost(Category category) {
        if (category.getTitle() == null || category.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (category.getDescription() == null || category.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

    }
}
