package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.usecases.MailjetEmailUseCase;
import com.example.demo.application.usecases.MedicamentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Medicamentos", description = "Operaciones relacionadas con los medicamentos")
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

    @Operation(summary = "Crear un nuevo medicamento", description = "Crea un nuevo medicamento con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicamento creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<MedicamentDTO> createMedicament(@RequestBody MedicamentDTO medicament){
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentUsecase.createMedicament(medicament));
    }

    @Operation(summary = "Obtener todos los medicamentos", description = "Obtiene una lista de todos los medicamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<MedicamentDTO>> getMedicaments(@RequestParam(required = false) String name){
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.getMedicamentsByNameContaining(name));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.getMedicaments());
        }
    }

    @Operation(summary = "Obtener un medicamento por ID", description = "Obtiene los detalles de un medicamento específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentDTO> getMedicamentById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.getMedicamentById(id));
    }

    @Operation(summary = "Actualizar un medicamento por ID", description = "Actualiza los datos de un medicamento específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicamento actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicamentDTO> updateMedicament(@PathVariable("id") Long id, @RequestBody MedicamentDTO medicamentDTO){
        return ResponseEntity.status(HttpStatus.OK).body(medicamentUsecase.updateMedicament(id, medicamentDTO));
    }

    @Operation(summary = "Eliminar un medicamento por ID", description = "Elimina un medicamento específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medicamento eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable("id") Long id){
        medicamentUsecase.getMedicamentById(id);
        medicamentUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
