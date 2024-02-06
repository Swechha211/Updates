package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.Category;

import java.util.List;

public interface CategoryService {
    public Category createCategory(Category category);
    public Category updateCategory(Category category, Integer categoryId);
    public void deleteCategory(Integer categoryId);
    Category getCategory(Integer categoryId);

    List<Category> getCategories();
}
