package com.example.blogwithsecurity.config;

import com.example.blogwithsecurity.service.UserInfoUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    //for getting username and password from database
    //authentication
    public UserDetailsService userDetailsService(){
        return new UserInfoUserDetailsService();
    }
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((authz) -> authz
//                .requestMatchers("/api")
                .anyRequest()
                .authenticated()
        );

        return  http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // This disables password encoding
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return  new BCryptPasswordEncoder();
//    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return  authenticationProvider;
//    }

    //practice

//    @Bean
//    public InMemoryUserDetailsManager userDetailsServive(){
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("swechha")
//                .password("swechha")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http.authorizeHttpRequests((authz) -> authz
//                .requestMatchers("/api")
////                .anyRequest()
//                .authenticated()
//        );
//
//        return  http.build();
//    }


    //hardcore username and password
    //authentication
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
//        UserDetails admin = User.withUsername("swechha")
//                .password(passwordEncoder.encode("swechha"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withUsername("swechhab")
//                .password(passwordEncoder.encode("swechhab"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }
//
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http.authorizeHttpRequests((authz) -> authz
//                .requestMatchers("/api")
////                .anyRequest()
//                .authenticated()
//        );
//
//        return  http.build();
//
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return  new BCryptPasswordEncoder();
//    }


}
