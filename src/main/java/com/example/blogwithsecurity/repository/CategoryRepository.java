package com.example.blogwithsecurity.repository;

import com.example.blogwithsecurity.entity.Category;
import com.example.blogwithsecurity.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}