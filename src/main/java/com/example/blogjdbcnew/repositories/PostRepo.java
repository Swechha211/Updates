package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Comment;
import com.example.blogjdbcnew.entities.Post;

import java.util.List;

public interface PostRepo {
    List<Post> findAll();
   Post findById(Integer id);
    void save(Post blog);
    void update(Post blog);
    void delete(Integer id);

    Post findByUserId(Integer userId);
}
