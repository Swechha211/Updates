package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.User;

import com.example.blogwithsecurity.utils.UserInfoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByName(username);
//        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserInfoDetails(user);

    }
}
