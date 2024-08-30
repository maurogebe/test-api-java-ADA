package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.VersionCompanyDTO;
import com.example.demo.application.usecases.VersionCompanyUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/version-company")
public class VersionCompanyController {

    private final VersionCompanyUseCase versionCompanyUseCase;

    @Autowired
    public VersionCompanyController(VersionCompanyUseCase versionCompanyUseCase) {
        this.versionCompanyUseCase = versionCompanyUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VersionCompanyDTO> getVersionCompanyById(@RequestParam Long id) {
        VersionCompanyDTO versionCompany = this.versionCompanyUseCase.getVersionCompanyById(id);
        return ResponseEntity.ok(versionCompany);
    }
    
    @GetMapping
    public ResponseEntity<List<VersionCompanyDTO>> getCompanies() {
        List<VersionCompanyDTO> versionCompanies = this.versionCompanyUseCase.getVersionCompanies();
        return ResponseEntity.ok(versionCompanies);
    }

    @PostMapping
    public ResponseEntity<VersionCompanyDTO> createVersionCompany(@Valid @RequestBody VersionCompanyDTO versionCompany) {
        VersionCompanyDTO versionCompanySaved = this.versionCompanyUseCase.createVersionCompany(versionCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(versionCompanySaved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVersionCompany(@RequestParam Long id, @Valid @RequestBody VersionCompanyDTO versionCompany) {
        this.versionCompanyUseCase.updateVersionCompany(id, versionCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body("Version Company updated successfully.");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVersionCompany(@RequestParam Long id) {
        this.versionCompanyUseCase.deleteVersionCompany(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Version Company deleted successfully.");
    }
}
