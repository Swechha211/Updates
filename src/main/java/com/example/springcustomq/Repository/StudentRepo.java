package com.example.springcustomq.Repository;

import com.example.springcustomq.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Integer> {

    @Query(value = "SELECT s FROM Student s")
    public List<Student> fetchAllFromCustom();

    @Query(value = "SELECT s FROM Student s where name=:val and mark=:m")
    public Student fetchUsingName(@Param("val") String name, @Param("m") int mark);

    @Query(value = "SELECT s FROM Student s where  mark=:m")
    public Student fetchUsingMark(@Param("m") int mark);


}
