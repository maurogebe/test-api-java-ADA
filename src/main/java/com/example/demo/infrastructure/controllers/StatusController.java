package com.example.demo.infrastructure.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<String> getStatus() {
        boolean dbStatus = checkDatabase();

        if (dbStatus) {
            return new ResponseEntity<>("API and Database are up", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Database is down", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private boolean checkDatabase() {
        try {
            // This query can be any simple query to test the connection
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
