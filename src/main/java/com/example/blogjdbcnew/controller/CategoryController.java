package com.example.blogjdbcnew.controller;

import com.example.blogjdbcnew.entities.Category;
import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.repositories.CategoryRepo;
import com.example.blogjdbcnew.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{catid}")
    public Category getCategoryById(@PathVariable Integer catid) {
        return categoryService.getCategoryById(catid);
    }

    @PostMapping("/")
    public void createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
    }

    @PutMapping("/{catid}")
    public void updateCategory(@PathVariable Integer catid, @RequestBody Category updatedCategory) {
        categoryService.updateCategory(catid, updatedCategory);
    }

    @DeleteMapping("/{catid}")
    public void deleteBlog(@PathVariable Integer catid) {
        categoryService.deleteBlog(catid);
    }

}
