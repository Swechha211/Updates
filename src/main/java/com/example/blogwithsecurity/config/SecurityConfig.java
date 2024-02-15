package com.example.blogwithsecurity.config;


import com.example.blogwithsecurity.security.CustomUserDetailService;
import com.example.blogwithsecurity.security.JwtAuthenticationEntryPoint;
import com.example.blogwithsecurity.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    //for getting username and password from database
    //authentication


    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf-> csrf.disable())
                .cors(cors-> cors.disable())
                .authorizeHttpRequests((authz) -> authz


                        .requestMatchers("/api/posts").authenticated()
//                        .requestMatchers("/api/comments").authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/users/"),
                                         new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().permitAll())
//                )
                        .exceptionHandling(ex-> ex.authenticationEntryPoint(point))
                        .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);


        return  http.build();

    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return  authenticationProvider;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // This disables password encoding
//    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }









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
