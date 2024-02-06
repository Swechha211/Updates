package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Post post, Integer userId, Integer categoryId);
    Post updatePost(Post post, Integer postId);
    void deletePost(Integer postId);
    List<Post> getAllPost();
    Post getPostById(Integer postId);
    List<Post> getPostByCategory(Integer categoryId);
    List<Post> getPostByUser(Integer userId);



}
