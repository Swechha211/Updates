package com.example.springcrud.repository;

import com.example.springcrud.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    public List<Course> findByFull(boolean full);

}
