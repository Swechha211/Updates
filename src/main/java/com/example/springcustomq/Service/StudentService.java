package com.example.springcustomq.Service;

import com.example.springcustomq.Entity.Student;
import com.example.springcustomq.Repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    public Student saveDetails(Student student){
        return studentRepo.save(student);
    }

    public List<Student> fetchAll(){
        return studentRepo.fetchAllFromCustom();
    }

    public Student fetchUsingName(){
        return studentRepo.fetchUsingName("Ram", 90);
    }

    public Student fetchUsingMArk(int mark){
        return studentRepo.fetchUsingMark(mark);
    }
}
