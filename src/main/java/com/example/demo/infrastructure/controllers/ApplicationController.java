package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.ApplicationDTO;
import com.example.demo.application.usecases.ApplicationUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationUseCase applicationUseCase;

    @Autowired
    public ApplicationController(ApplicationUseCase applicationUseCase) {
        this.applicationUseCase = applicationUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@RequestParam Long id) {
        ApplicationDTO application = this.applicationUseCase.getApplicationById(id);
        return ResponseEntity.ok(application);
    }
    
    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getApplications() {
        List<ApplicationDTO> applications = this.applicationUseCase.getApplications();
        return ResponseEntity.ok(applications);
    }

    @PostMapping
    public ResponseEntity<ApplicationDTO> createApplication(@Valid @RequestBody ApplicationDTO application) {
        ApplicationDTO applicationSaved = this.applicationUseCase.createApplication(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationSaved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateApplication(@RequestParam Long id, @Valid @RequestBody ApplicationDTO application) {
        this.applicationUseCase.updateApplication(id, application);
        return ResponseEntity.status(HttpStatus.CREATED).body("Application updated successfully.");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@RequestParam Long id) {
        this.applicationUseCase.deleteApplication(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Application deleted successfully.");
    }
}
