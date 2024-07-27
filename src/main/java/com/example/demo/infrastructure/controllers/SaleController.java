package com.example.demo.infrastructure.controllers;
import com.example.demo.application.dtos.SaleWithMedicamentDTO;
import com.example.demo.domain.repositories.ISaleRepository;
import com.example.demo.application.usecases.GeneratePDFUseCase;
import com.example.demo.application.usecases.SaleUseCase;
import com.example.demo.domain.entities.Sale;
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
public class SaleController {

    private final SaleUseCase saleUseCase;
    private final GeneratePDFUseCase generatePDFUsecase;
    private final ISaleRepository iSaleRepository;

    @Autowired
    public SaleController(SaleUseCase saleUseCase, GeneratePDFUseCase generatePDFUsecase, ISaleRepository iSaleRepository) {
        this.saleUseCase = saleUseCase;
        this.generatePDFUsecase = generatePDFUsecase;
        this.iSaleRepository = iSaleRepository;
    }

    @PostMapping
    public ResponseEntity<SaleWithMedicamentDTO> createSale(@RequestBody SaleWithMedicamentDTO sale){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.createSale(sale));
    }

    @GetMapping
    public ResponseEntity<List<SaleWithMedicamentDTO>> getSales(){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.getSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleWithMedicamentDTO> viewSale(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.getSaleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleById(@PathVariable("id") Long id){
        saleUseCase.getSaleById(id);
        saleUseCase.deleteSaleById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<SaleWithMedicamentDTO> updateSale(@PathVariable("id") Long id, @RequestBody SaleWithMedicamentDTO sale){
        SaleWithMedicamentDTO saleUpdated = saleUseCase.updateSale(id, sale);
        return ResponseEntity.ok(saleUpdated);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<ByteArrayResource> getPdf(@PathVariable("id") Long id){
        Sale sale = iSaleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));

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
