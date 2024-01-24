package com.example.blogjdbcnew.services;


import com.example.blogjdbcnew.entities.User;
import com.example.blogjdbcnew.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id);
    }

    public void createUser(User user) {
        userRepo.create(user);
        validateUser( user);
    }

    public void updateUser(Integer id, User updatedUser) {


        User existingUser = userRepo.findById(id);
        validateUser( existingUser );
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setAbout(updatedUser.getAbout());
            userRepo.update(existingUser);
        }
    }

    public void deleteUser(Integer id) {
        userRepo.delete(id);
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (user.getAbout() == null || user.getAbout().trim().isEmpty()) {
            throw new IllegalArgumentException("Write about yourself");
        }

    }

}
