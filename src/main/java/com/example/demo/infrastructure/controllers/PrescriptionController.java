package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.PrescriptionWithMedicamentDTO;
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
    public ResponseEntity<PrescriptionWithMedicamentDTO> createPrescription(@RequestBody PrescriptionWithMedicamentDTO prescription){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionUseCase.createPrescription(prescription));
    }

    @GetMapping
    public ResponseEntity<PrescriptionWithMedicamentDTO> getPrescriptionById (@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionUseCase.getPrescriptionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionWithMedicamentDTO> updatePrescription (@PathVariable("id") Long id, @RequestBody PrescriptionWithMedicamentDTO prescription){
        PrescriptionWithMedicamentDTO prescriptionUpdated = prescriptionUseCase.updatePrescription(id, prescription);
        return ResponseEntity.ok(prescriptionUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescriptionById(@PathVariable("id") Long id){
        prescriptionUseCase.deletePrescriptionById(id);
        return ResponseEntity.noContent().build();
    }

}
