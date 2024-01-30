package com.example.blogjdbcnew.controller;

import com.example.blogjdbcnew.entities.Comment;
import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.repositories.CommentRepo;
import com.example.blogjdbcnew.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{commentId}")
    public Comment getCommentById(@PathVariable Integer commentId) {
        return commentService.getCommentById(commentId);
    }

//    @GetMapping("//{userId}")
//    public Comment getByUserId(@PathVariable Integer userId) {
//        return commentService.getByUserId(userId);
//    }

    @GetMapping("/users/{userId}")
    public List<Comment> getByUserId(Integer userId) {
        return commentService.getByUserId(userId);
    }

    @PostMapping("/")
    public void createComment(@RequestBody Comment comment) {
        commentService.createComment(comment);
    }

    @PutMapping("/{commentId}")
    public void updateComment(@PathVariable Integer commentId, @RequestBody Comment updatedComment) {
        commentService.updateComment(commentId,updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }

}
