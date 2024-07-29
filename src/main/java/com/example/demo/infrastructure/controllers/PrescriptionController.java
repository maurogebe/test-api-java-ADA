package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.PrescriptionWithMedicamentDTO;
import com.example.demo.application.usecases.PrescriptionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Prescripciones", description = "Operaciones relacionadas con las prescripciones médicas")
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    private PrescriptionUseCase prescriptionUseCase;

    @Autowired
    public PrescriptionController(PrescriptionUseCase prescriptionUseCase) {
        this.prescriptionUseCase = prescriptionUseCase;
    }

    @Operation(summary = "Crear una nueva prescripción", description = "Crea una nueva prescripción con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescripción creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PrescriptionWithMedicamentDTO> createPrescription(@RequestBody PrescriptionWithMedicamentDTO prescription){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionUseCase.createPrescription(prescription));
    }

    @Operation(summary = "Obtener una prescripción por ID", description = "Obtiene los detalles de una prescripción específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Prescripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionWithMedicamentDTO> getPrescriptionById (@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionUseCase.getPrescriptionById(id));
    }

    @Operation(summary = "Actualizar una prescripción por ID", description = "Actualiza los datos de una prescripción específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescripción actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Prescripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionWithMedicamentDTO> updatePrescription (@PathVariable("id") Long id, @RequestBody PrescriptionWithMedicamentDTO prescription){
        PrescriptionWithMedicamentDTO prescriptionUpdated = prescriptionUseCase.updatePrescription(id, prescription);
        return ResponseEntity.ok(prescriptionUpdated);
    }

    @Operation(summary = "Eliminar una prescripción por ID", description = "Elimina una prescripción específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescripción eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Prescripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescriptionById(@PathVariable("id") Long id){
        prescriptionUseCase.deletePrescriptionById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Leer PDF o imagen con OCR", description = "Leer PDF o imagen con tecnicas OCR y obtener la prescripcion y guardarla")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescripción obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/ocr")
    public ResponseEntity<PrescriptionWithMedicamentDTO> getPrescriptionWithFile(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(prescriptionUseCase.getPrescriptionWithFile(file));
    }

}
