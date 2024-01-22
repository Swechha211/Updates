package com.example.blogjdbcnew.controller;

import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class PostController {

        @Autowired
        private PostRepo postRepo;

        @GetMapping("/")
        public List<Post> getAllBlogs() {
            return postRepo.findAll();
        }

        @GetMapping("/{id}")
        public Post getBlogById(@PathVariable Integer id) {
            return postRepo.findById(id);
        }

        @PostMapping("/")
        public void createBlog(@RequestBody Post blog) {
            postRepo.save(blog);
        }

        @PutMapping("/{id}")
        public void updateBlog(@PathVariable Integer id, @RequestBody Post updatedBlog) {
           Post existingBlog = postRepo.findById(id);
            if (existingBlog != null) {
                existingBlog.setTitle(updatedBlog.getTitle());
                existingBlog.setContent(updatedBlog.getContent());
                postRepo.update(existingBlog);
            }
        }

        @DeleteMapping("/{id}")
        public void deleteBlog(@PathVariable Integer id) {
            postRepo.delete(id);
        }

}
