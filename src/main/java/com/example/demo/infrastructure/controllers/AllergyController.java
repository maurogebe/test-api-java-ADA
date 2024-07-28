package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.AllergyDTO;
import com.example.demo.application.usecases.AllergyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Alergias", description = "Operaciones relacionadas con los alergias")
@RestController
@RequestMapping("/allergy")
public class AllergyController {

    private final AllergyUseCase allergyUseCase;

    @Autowired
    public AllergyController(AllergyUseCase allergyUseCase){
        this.allergyUseCase = allergyUseCase;
    }

    @Operation(summary = "Crear una nueva alergia", description = "Crea una nueva alergia con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alergia creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<AllergyDTO> createAllergy(@RequestBody AllergyDTO patientRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyUseCase.createAllergy(patientRequestDTO));
    }

    @Operation(summary = "Obtener todas las alergias", description = "Obtiene una lista de todas las alergias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Set<AllergyDTO>> getAllergies(){
        return ResponseEntity.status(HttpStatus.OK).body(allergyUseCase.getAllergies());
    }

    @Operation(summary = "Obtener una alergia por ID", description = "Obtiene los detalles de una alergia específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Alergia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AllergyDTO> getAllergyById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(allergyUseCase.getAllergyById(id));
    }

    @Operation(summary = "Actualizar una alergia por ID", description = "Actualiza los datos de una alergia específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alergia actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Alergia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AllergyDTO> updateAllergy(@PathVariable("id") Long id, @RequestBody AllergyDTO patientDTO){
        return ResponseEntity.status(HttpStatus.OK).body(allergyUseCase.updateAllergy(id, patientDTO));
    }

    @Operation(summary = "Eliminar una alergia por ID", description = "Elimina una alergia específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alergia eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Alergia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable("id") Long id) {
        allergyUseCase.getAllergyById(id);
        allergyUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
