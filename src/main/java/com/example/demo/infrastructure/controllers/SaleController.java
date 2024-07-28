package com.example.demo.infrastructure.controllers;
import com.example.demo.application.dtos.SaleWithMedicamentDTO;
import com.example.demo.domain.repositories.SaleRepository;
import com.example.demo.application.usecases.GeneratePDFUseCase;
import com.example.demo.application.usecases.SaleUseCase;
import com.example.demo.domain.entities.Sale;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.mailjet.client.errors.MailjetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
@Tag(name = "Ventas", description = "Operaciones relacionadas con las ventas en QuickPharma")
public class SaleController {

    private final SaleUseCase saleUseCase;
    private final GeneratePDFUseCase generatePDFUsecase;
    private final SaleRepository saleRepository;

    @Autowired
    public SaleController(SaleUseCase saleUseCase, GeneratePDFUseCase generatePDFUsecase, SaleRepository saleRepository) {
        this.saleUseCase = saleUseCase;
        this.generatePDFUsecase = generatePDFUsecase;
        this.saleRepository = saleRepository;
    }

    @Operation(summary = "Crear una nueva venta", description = "Crea una nueva venta con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<SaleWithMedicamentDTO> createSale(@RequestBody SaleWithMedicamentDTO sale) throws MailjetException {
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.createSale(sale));
    }

    @Operation(summary = "Obtener todas las ventas", description = "Obtiene una lista de todas las ventas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Ventas no encontradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<SaleWithMedicamentDTO>> getSales(){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.getSales());
    }

    @Operation(summary = "Obtener una venta por ID", description = "Obtiene los detalles de una venta específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleWithMedicamentDTO> viewSale(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.getSaleById(id));
    }

    @Operation(summary = "Eliminar una venta por ID", description = "Elimina una venta específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venta eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleById(@PathVariable("id") Long id){
        saleUseCase.getSaleById(id);
        saleUseCase.deleteSaleById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar una venta por ID", description = "Actualiza los datos de una venta específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SaleWithMedicamentDTO> updateSale(@PathVariable("id") Long id, @RequestBody SaleWithMedicamentDTO sale){
        SaleWithMedicamentDTO saleUpdated = saleUseCase.updateSale(id, sale);
        return ResponseEntity.ok(saleUpdated);
    }

    @Operation(summary = "Generar PDF de una venta por ID", description = "Genera un PDF con los detalles de una venta específica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF generado con éxito"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/pdf")
    public ResponseEntity<ByteArrayResource> getPdf(@PathVariable("id") Long id){
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));

        ModelMap model = new ModelMap();

        model.addAttribute("sale", sale);
        byte[] pdf = generatePDFUsecase.createPdf("saleDetails", model);

        ByteArrayResource resource = new ByteArrayResource(pdf);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sale_" + id + ".pdf");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(pdf.length)
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }

}
