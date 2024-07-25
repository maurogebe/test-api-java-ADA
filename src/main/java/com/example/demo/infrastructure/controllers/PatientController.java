package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.PatientUsecase;
import com.example.demo.domain.dtos.PatientRequestDTO;
import com.example.demo.domain.dtos.PatientResponseDTO;
import com.example.demo.domain.dtos.PatientUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientUsecase patientUsecase;

    @Autowired
    public PatientController(PatientUsecase patientUsecase){
        this.patientUsecase = patientUsecase;
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody PatientRequestDTO patientRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientUsecase.createPatient(patientRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> searchPatient(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.searchPatient(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@RequestBody PatientUpdateRequestDTO patientUpdateRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.updatePatient(patientUpdateRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientUsecase.searchPatient(id);
        patientUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
