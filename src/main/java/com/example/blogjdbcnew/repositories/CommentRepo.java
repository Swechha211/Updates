package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Comment;
import com.example.blogjdbcnew.entities.Post;

import java.util.List;

public interface CommentRepo {

    List<Comment> findAll();
    Comment findById(Integer commentId);
    void save(Comment comment);
    void update(Comment comment);
    void delete(Integer commentId);
}
