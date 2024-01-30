package com.example.blogjdbcnew.services;

import com.example.blogjdbcnew.entities.Category;
import com.example.blogjdbcnew.entities.Comment;
import com.example.blogjdbcnew.repositories.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    public List<Comment> getAllComments() {
        return commentRepo.findAll();
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepo.findById(commentId);
    }

    public void createComment(Comment comment) {
        validateComment(comment);
        commentRepo.save(comment);
    }

    public void updateComment(Integer commentId, Comment updatedComment) {
        Comment existingComment = commentRepo.findById(commentId);
        validateComment(existingComment);
        if (existingComment != null) {
            existingComment.setContent(updatedComment.getContent());
            existingComment.setAddedDate(updatedComment.getAddedDate());
            commentRepo.update(existingComment);
        }
    }

//    public Comment getByUserId(Integer userId) {
//        return commentRepo.findByUserId(userId);
//    }

    public List<Comment> getByUserId(Integer userId) {
        return commentRepo.findByUser(userId);
    }

    public void deleteComment(Integer commentId) {
        commentRepo.delete(commentId);
    }

    private void validateComment(Comment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        if (comment.getAddedDate() == null ) {
            throw new IllegalArgumentException("Date cannot be empty");
        }

    }
}
