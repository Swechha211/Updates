package com.example.blogwithsecurity.controller;

import com.example.blogwithsecurity.entity.User;

import com.example.blogwithsecurity.service.UserService;
import com.example.blogwithsecurity.utils.ApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUser = this.userService.findAll();
        return new ResponseEntity<List<User>>(allUser, HttpStatus.OK) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user1 = this.userService.findById(id);
        return new ResponseEntity<User>(user1, HttpStatus.OK) ;

    }

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User creatUser = this.userService.create(user);
        return new ResponseEntity<User>(creatUser, HttpStatus.CREATED) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User updateUser = this.userService.update(updatedUser, id);
        return  ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return new ResponseEntity(new ApiResource("User deleted successfully", true), HttpStatus.OK);
    }

}
