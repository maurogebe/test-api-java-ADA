package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.MedicamentUsecase;
import com.example.demo.domain.dtos.MedicamentRequestDTO;
import com.example.demo.domain.dtos.MedicamentResponseDTO;
import com.example.demo.domain.dtos.MedicamentUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    private final MedicamentUsecase medicamentUsecase;

    @Autowired
    public MedicamentController(MedicamentUsecase medicamentUsecase){
        this.medicamentUsecase = medicamentUsecase;
    }

    @PostMapping
    public ResponseEntity<MedicamentResponseDTO> createMedicament(@RequestBody MedicamentRequestDTO medicamentRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentUsecase.createMedicament(medicamentRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<MedicamentResponseDTO>> getAllMedicaments(){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentResponseDTO> getMedicament(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.searchMedicament(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentResponseDTO> updateMedicament(@RequestBody MedicamentUpdateRequestDTO medicamentUpdateRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.updateMedicament(medicamentUpdateRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable("id") Long id){
        medicamentUsecase.searchMedicament(id);
        medicamentUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
