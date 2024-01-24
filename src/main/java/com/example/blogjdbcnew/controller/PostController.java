package com.example.blogjdbcnew.controller;

import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.repositories.PostRepo;
import com.example.blogjdbcnew.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public List<Post> getAllBlogs() {
        return postService.getAllBlogs();
    }

    @GetMapping("/{id}")
    public Post getBlogById(@PathVariable Integer id) {
        return postService.getBlogById(id);
    }

    @PostMapping("/")
    public void createBlog(@RequestBody Post blog) {
        postService.createBlog(blog);
    }

    @PutMapping("/{id}")
    public void updateBlog(@PathVariable Integer id, @RequestBody Post updatedBlog) {
        postService.updateBlog(id, updatedBlog);

    }

    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable Integer id) {
        postService.deleteBlog(id);
    }

}
