package com.example.blogwithsecurity.security;

import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.exceptation.ResourceNotFoundException;
import com.example.blogwithsecurity.repository.UserRepository;
import com.example.blogwithsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {


    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = this.userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found "+ username));
        return user;
    }
}
