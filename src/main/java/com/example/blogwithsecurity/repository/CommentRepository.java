package com.example.blogwithsecurity.repository;

import com.example.blogwithsecurity.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
