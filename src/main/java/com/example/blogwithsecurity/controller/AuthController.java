package com.example.blogwithsecurity.controller;

import com.example.blogwithsecurity.entity.JwtRequest;
import com.example.blogwithsecurity.entity.JwtResponse;
import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.security.JwtTokenHelper;
import com.example.blogwithsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

   @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtTokenHelper helper;

    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return  new ResponseEntity<>(response, HttpStatus.OK);


    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try{
            manager.authenticate(authentication);
        }catch(BadCredentialsException e){
            throw new RuntimeException(("Invalid Username or password"));
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){
        return"Credentials invalid ";
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User userDto) {
        User registeredUser = this.userService.registerNewUser(userDto);
        return  new ResponseEntity<User>(registeredUser, HttpStatus.CREATED);



    }


}
