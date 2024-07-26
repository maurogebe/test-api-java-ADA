package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.MailjetEmailUseCase;
import com.example.demo.application.usecases.MedicamentUsecase;
import com.example.demo.domain.dtos.MedicamentRequestDTO;
import com.example.demo.domain.dtos.MedicamentResponseDTO;
import com.example.demo.domain.dtos.MedicamentUpdateRequestDTO;
import com.example.demo.domain.entities.Medicament;
import com.mailjet.client.errors.MailjetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicament")
public class MedicamentController {

    private final MedicamentUsecase medicamentUsecase;
    private final MailjetEmailUseCase mailjetEmailUseCase;

    @Autowired
    public MedicamentController(MedicamentUsecase medicamentUsecase, MailjetEmailUseCase mailjetEmailUseCase){
        this.medicamentUsecase = medicamentUsecase;
        this.mailjetEmailUseCase = mailjetEmailUseCase;
    }

    @PostMapping
    public ResponseEntity<Medicament> createMedicament(@RequestBody Medicament medicament){
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentUsecase.createMedicament(medicament));
    }

    @GetMapping
    public ResponseEntity<List<Medicament>> getAllMedicaments(){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicament> getMedicament(@PathVariable("id") Long id){
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
