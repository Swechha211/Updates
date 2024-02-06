package com.example.blogwithsecurity.controller;

import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.payloads.UserDto;
import com.example.blogwithsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
       return userService.update(updatedUser, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.delete(id);
    }

}
