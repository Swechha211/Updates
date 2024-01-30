package com.example.blogjdbcnew.controller;

import com.example.blogjdbcnew.entities.User;
import com.example.blogjdbcnew.repositories.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordingRepository recordingRepository;

    @PostMapping("/")
    public void addingRecord(@RequestBody User user) {
        recordingRepository.addRecord(user);
    }

}
