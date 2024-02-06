package com.example.blogwithsecurity.controller;

import com.example.blogwithsecurity.entity.Post;
import com.example.blogwithsecurity.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.blogwithsecurity.utils.ApiResource;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    //create post
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<Post> createPost(
            @RequestBody Post post,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId
    )
    {
        Post createPost = this.postService.createPost(post, userId, categoryId);
        return new ResponseEntity<Post>(createPost, HttpStatus.CREATED);
    }

    //get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPost(){
        List<Post> allPost = this.postService.getAllPost();
        return  new ResponseEntity<List<Post>>(allPost, HttpStatus.OK);
    }

    //get  post by id
    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer postId){
        Post postDto = this.postService.getPostById(postId);
        return  new ResponseEntity<Post>(postDto, HttpStatus.OK);
    }

    // delete post

    @DeleteMapping("/posts/{postId}")
    public ApiResource deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ApiResource("Post is successfully deleted!!", true);
    }

    // update post
    @PutMapping("/posts/{postId}")
    public ResponseEntity<Post> updatePost(@RequestBody Post post,@PathVariable Integer postId){
        Post updatePost = this.postService.updatePost(post, postId);
        return  new ResponseEntity<Post>(updatePost, HttpStatus.OK);
    }

    //get by category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<Post>>getPostByCategory(@PathVariable Integer categoryId
    ){
        List<Post> posts = this.postService.getPostByCategory(categoryId);
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);

    }

    //get by user
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<Post>>getPostByUser(@PathVariable Integer userId
    ){
        List<Post> posts = this.postService.getPostByUser(userId);
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);

    }

}
