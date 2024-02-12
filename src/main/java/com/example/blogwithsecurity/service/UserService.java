package com.example.blogwithsecurity.service;



import com.example.blogwithsecurity.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public interface UserService {

    User create(User user);
    User update(User user, Integer userId);
    User findById(Integer userId);
    List<User> findAll();
    void delete(Integer userId);
    User findByName(String username);

}
