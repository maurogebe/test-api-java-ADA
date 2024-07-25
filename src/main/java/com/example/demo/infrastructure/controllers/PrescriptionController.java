package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.PrescriptionUseCase;
import com.example.demo.domain.entities.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    private PrescriptionUseCase prescriptionUseCase;
    @Autowired
    public PrescriptionController(PrescriptionUseCase prescriptionUseCase) {
        this.prescriptionUseCase = prescriptionUseCase;
    }

    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionUseCase.createPrescription(prescription));
    }

    @GetMapping
    public ResponseEntity<Prescription> searchPrescription (@PathVariable("id")Long id){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionUseCase.fintByIDPrescription(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> viewPrescription(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionUseCase.fintByIdPrescription(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePrescription (@PathVariable("id")Long id, @RequestBody Prescription prescription){
        prescriptionUseCase.updatePrescription(id, prescription);
        return ResponseEntity.ok("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByIdPrescription(@PathVariable("id")Long id){
        prescriptionUseCase.fintByIdPrescription(id);
        prescriptionUseCase.deleteByIdPrescription(id);
        return ResponseEntity.noContent().build();
    }

}
