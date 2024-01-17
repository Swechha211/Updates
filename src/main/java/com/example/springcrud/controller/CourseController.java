package com.example.springcrud.controller;

import com.example.springcrud.entity.Course;
import com.example.springcrud.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

@GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable long id){
        try {
            Optional<Course> course = courseRepository.findById(id);
            if (course.isPresent()) {
                return new ResponseEntity<>(course.get(),HttpStatus.OK);
            } else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch(Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
@PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable long id, @RequestBody Course course){
        Optional<Course> courseData = courseRepository.findById(id);
        if(courseData.isPresent()){
            Course _course = courseData.get();
            _course.setTitle(course.getTitle());
            _course.setDescription(course.getDescription());
            course.setFull(course.isFull());

            return new ResponseEntity<>(courseRepository.save(_course), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable long id){
        try{
            courseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
@DeleteMapping("/courses")
    public ResponseEntity<HttpStatus> deleteAllCourse() {
        try {
            courseRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@GetMapping("/course/full")
    public ResponseEntity<List<Course>> findByFull(){
        try {
            List<Course> courseList = courseRepository.findByFull(true);
            if (courseList == null || courseList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
