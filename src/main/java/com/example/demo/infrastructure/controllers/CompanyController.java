package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.CompanyDTO;
import com.example.demo.application.dtos.CompanyResponseWithApplicationDTO;
import com.example.demo.application.dtos.CompanyWithVersionDTO;
import com.example.demo.application.usecases.CompanyUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyUseCase companyUseCase;

    @Autowired
    public CompanyController(CompanyUseCase companyUseCase) {
        this.companyUseCase = companyUseCase;
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<CompanyResponseWithApplicationDTO> getCompanyByCode(@RequestParam String code) {
        CompanyResponseWithApplicationDTO company = this.companyUseCase.getCompanyByCode(code);
        return ResponseEntity.ok(company);
    }
    
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getCompanies() {
        List<CompanyDTO> companies = this.companyUseCase.getCompanies();
        return ResponseEntity.ok(companies);
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO company) {
        CompanyDTO companySaved = this.companyUseCase.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(companySaved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@RequestParam Long id, @Valid @RequestBody CompanyDTO company) {
        this.companyUseCase.updateCompany(id, company);
        return ResponseEntity.status(HttpStatus.CREATED).body("Company updated successfully.");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@RequestParam Long id) {
        this.companyUseCase.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Company deleted successfully.");
    }
}
