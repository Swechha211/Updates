package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Category;
import com.example.blogjdbcnew.entities.Post;

import java.util.List;

public interface CategoryRepo {
    List<Category> findAll();
    Category findById(Integer catid);
    void save(Category category);
    void update(Category category);
    void delete(Integer catid);
}
