package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.PatientUseCase;
import com.example.demo.application.dtos.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientUseCase patientUsecase;

    @Autowired
    public PatientController(PatientUseCase patientUsecase){
        this.patientUsecase = patientUsecase;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientUsecase.createPatient(patientRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients(){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.getPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.getPatientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable("id") Long id, @RequestBody PatientDTO patientDTO){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.updatePatient(id, patientDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientUsecase.getPatientById(id);
        patientUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
