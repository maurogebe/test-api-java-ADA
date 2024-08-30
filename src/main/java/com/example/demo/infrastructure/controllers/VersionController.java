package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.VersionDTO;
import com.example.demo.application.usecases.VersionUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/version")
public class VersionController {

    private final VersionUseCase versionUseCase;

    @Autowired
    public VersionController(VersionUseCase versionUseCase) {
        this.versionUseCase = versionUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VersionDTO> getVersionById(@RequestParam Long id) {
        VersionDTO version = this.versionUseCase.getVersionById(id);
        return ResponseEntity.ok(version);
    }
    
    @GetMapping
    public ResponseEntity<List<VersionDTO>> getVersions() {
        List<VersionDTO> versions = this.versionUseCase.getVersions();
        return ResponseEntity.ok(versions);
    }

    @PostMapping
    public ResponseEntity<VersionDTO> createVersion(@Valid @RequestBody VersionDTO version) {
        VersionDTO versionSaved = this.versionUseCase.createVersion(version);
        return ResponseEntity.status(HttpStatus.CREATED).body(versionSaved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVersion(@RequestParam Long id, @Valid @RequestBody VersionDTO version) {
        this.versionUseCase.updateVersion(id, version);
        return ResponseEntity.status(HttpStatus.CREATED).body("Version updated successfully.");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVersion(@RequestParam Long id) {
        this.versionUseCase.deleteVersion(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Version deleted successfully.");
    }
}
