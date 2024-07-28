package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.PatientUseCase;
import com.example.demo.application.dtos.PatientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pacientes", description = "Operaciones relacionadas con los pacientes")
@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientUseCase patientUsecase;

    @Autowired
    public PatientController(PatientUseCase patientUsecase){
        this.patientUsecase = patientUsecase;
    }

    @Operation(summary = "Crear un nuevo paciente", description = "Crea un nuevo paciente con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientUsecase.createPatient(patientRequestDTO));
    }

    @Operation(summary = "Obtener todos los pacientes", description = "Obtiene una lista de todos los pacientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients(){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.getPatients());
    }

    @Operation(summary = "Obtener un paciente por ID", description = "Obtiene los detalles de un paciente específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.getPatientById(id));
    }

    @Operation(summary = "Actualizar un paciente por ID", description = "Actualiza los datos de un paciente específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable("id") Long id, @RequestBody PatientDTO patientDTO){
        return ResponseEntity.status(HttpStatus.OK).body(patientUsecase.updatePatient(id, patientDTO));
    }

    @Operation(summary = "Eliminar un paciente por ID", description = "Elimina un paciente específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientUsecase.getPatientById(id);
        patientUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
