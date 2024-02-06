package com.example.blogwithsecurity.service;



import com.example.blogwithsecurity.entity.User;

import java.util.List;

public interface UserService {

    User create(User user);
    User update(User user, Integer userId);
    User findById(Integer userId);
    List<User> findAll();
    void delete(Integer userId);
}
