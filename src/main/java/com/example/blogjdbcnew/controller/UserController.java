package com.example.blogjdbcnew.controller;

import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.entities.User;
import com.example.blogjdbcnew.repositories.PostRepo;
import com.example.blogjdbcnew.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public List<User> getAllUsers() {

        return userRepo.findAll();
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userRepo.findById(id);
    }

    @PostMapping("/")
    public void createUser(@RequestBody User user) {
        userRepo.create(user);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User existingUser = userRepo.findById(id);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setAbout(updatedUser.getAbout());
            userRepo.update(existingUser);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepo.delete(id);
    }



}
