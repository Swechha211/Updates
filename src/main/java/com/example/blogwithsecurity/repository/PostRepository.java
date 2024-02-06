package com.example.blogwithsecurity.repository;

import com.example.blogwithsecurity.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {
}
