//package com.example.blogwithsecurity.service;
//
//import com.example.blogwithsecurity.entity.User;
//
//import com.example.blogwithsecurity.exceptation.UserNotFoundException;
//import com.example.blogwithsecurity.utils.UserInfoDetails;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class UserInfoUserDetailsService implements UserDetailsService {
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
//        User user = userService.findByName(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found: " + username);
//        }
//
//        return new UserInfoDetails(user);
//
//    }
//}
