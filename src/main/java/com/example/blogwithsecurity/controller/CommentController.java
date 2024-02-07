package com.example.blogwithsecurity.controller;

import com.example.blogwithsecurity.entity.Comment;
import com.example.blogwithsecurity.entity.Post;
import com.example.blogwithsecurity.service.CommentService;
import com.example.blogwithsecurity.service.PostService;
import com.example.blogwithsecurity.utils.ApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //create post
    @PostMapping("/user/{userId}/post/{postId}/comments")
    public ResponseEntity<Comment> createComment(
            @RequestBody Comment comment,
            @PathVariable Integer userId,
            @PathVariable Integer postId
    ){
        Comment createComment = this.commentService.createComment(comment, userId, postId);
        return new ResponseEntity<Comment>(createComment, HttpStatus.CREATED);
    }

    //get all comments
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComment(){
        List<Comment> allComment = this.commentService.getAllComment();
        return  new ResponseEntity<List<Comment>>(allComment, HttpStatus.OK);
    }

    //get  comment by id
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer commentId) {
        Comment comment = this.commentService.getCommentById(commentId);
        return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }

    //delete comment
    @DeleteMapping("/comments/{commentId}")
    public ApiResource deleteComment(@PathVariable Integer commentId){
        this.commentService.deleteComment(commentId);
        return new ApiResource("Comment is successfully deleted!!", true);
    }

    // update comment
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment,@PathVariable Integer commentId){
        Comment updateComment = this.commentService.updateComment(comment, commentId);
        return  new ResponseEntity<Comment>(updateComment, HttpStatus.OK);
    }

    //get by post
    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<Comment>>getCommentByPost(@PathVariable Integer postId
    ){
        List<Comment> comments = this.commentService.getCommentByPost(postId);
        return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);

    }

    //get by user
    @GetMapping("/user/{userId}/comments")
    public ResponseEntity<List<Comment>>getCommentByUser(@PathVariable Integer userId
    ){
        List<Comment> comments = this.commentService.getCommentByUser(userId);
        return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);

    }


}
