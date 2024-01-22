package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.User;

import java.util.List;

public interface UserRepo {
   void create(User user);
    void update(User user);
    User findById(Integer userId);
    List<User> findAll();
    void delete(Integer userId);
}
