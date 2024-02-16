package com.example.blogwithsecurity.service;



import com.example.blogwithsecurity.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public interface UserService {

    User registerNewUser(User user);
    User create(User user);
    User update(User user, Integer userId);
    User findById(Integer userId);
    List<User> findAll();
    void delete(Integer userId);
    Optional<User>findByName(String username);
    User findByNameandPassword(String username ,String password);
    Optional<User> findByEmail(String email);

}
