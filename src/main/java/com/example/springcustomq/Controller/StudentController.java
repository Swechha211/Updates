package com.example.springcustomq.Controller;

import com.example.springcustomq.Entity.Student;
import com.example.springcustomq.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/addStudent")
    public Student postDetails(@RequestBody Student student){
        return studentService.saveDetails(student);

    }
    @GetMapping("/customfetch")
    public List<Student> fetchController(){
        return studentService.fetchAll();

    }

    @GetMapping("/customName")
    public Student fetchName(){
        return studentService.fetchUsingName();

    }

    @GetMapping("/customMark/{mark}")
    public Student fetchMark(@PathVariable int mark){
        return studentService.fetchUsingMArk(mark);

    }

}
