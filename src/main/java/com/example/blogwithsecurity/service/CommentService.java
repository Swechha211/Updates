package com.example.blogwithsecurity.service;


import com.example.blogwithsecurity.entity.Category;
import com.example.blogwithsecurity.entity.Comment;
import com.example.blogwithsecurity.entity.Post;

import java.util.List;

public interface CommentService {

    Comment createComment(Comment comment, Integer userId, Integer postId);
    Comment updateComment(Comment comment, Integer commentId);
    void deleteComment(Integer commentId);
    List<Comment> getAllComment();
   Comment getCommentById(Integer commentId);
    List<Comment> getCommentByPost(Integer postId);
    List<Comment> getCommentByUser(Integer userId);
}
