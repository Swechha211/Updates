package com.example.springcrud.controller;

import com.example.springcrud.entity.Course;
import com.example.springcrud.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    private CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }
@PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course){
        try {
            Course newCOurse = courseRepository.save(new Course(course.getTitle(), course.getDescription(), course.isFull()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
@GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourse(){
        try {
            List<Course> courseList = courseRepository.findAll();
            if (courseList == null || courseList.isEmpty()){
               return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
