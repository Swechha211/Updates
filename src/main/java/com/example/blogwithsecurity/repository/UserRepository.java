package com.example.blogwithsecurity.repository;

import com.example.blogwithsecurity.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer> {



}
