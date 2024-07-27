package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.usecases.MailjetEmailUseCase;
import com.example.demo.application.usecases.MedicamentUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicament")
public class MedicamentController {

    private final MedicamentUseCase medicamentUsecase;
    private final MailjetEmailUseCase mailjetEmailUseCase;

    @Autowired
    public MedicamentController(MedicamentUseCase medicamentUsecase, MailjetEmailUseCase mailjetEmailUseCase){
        this.medicamentUsecase = medicamentUsecase;
        this.mailjetEmailUseCase = mailjetEmailUseCase;
    }

    @PostMapping
    public ResponseEntity<MedicamentDTO> createMedicament(@RequestBody MedicamentDTO medicament){
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentUsecase.createMedicament(medicament));
    }

    @GetMapping
    public ResponseEntity<List<MedicamentDTO>> getMedicaments(){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.getMedicaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentDTO> getMedicamentById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.getMedicamentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentDTO> updateMedicament(@PathVariable("id") Long id, @RequestBody MedicamentDTO medicamentDTO){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.updateMedicament(id, medicamentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable("id") Long id){
        medicamentUsecase.getMedicamentById(id);
        medicamentUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
