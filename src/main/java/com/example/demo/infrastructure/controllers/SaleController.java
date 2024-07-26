package com.example.demo.infrastructure.controllers;
import com.example.demo.domain.repositories.ISaleRepository;
import com.example.demo.application.usecases.GeneratePDFUsecase;
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
@RequestMapping("/sales")
public class SaleController {

    private final SaleUseCase saleUseCase;
    private final GeneratePDFUsecase generatePDFUsecase;
    private final ISaleRepository iSaleRepository;

    @Autowired
    public SaleController(SaleUseCase saleUseCase, GeneratePDFUsecase generatePDFUsecase, ISaleRepository iSaleRepository) {
        this.saleUseCase = saleUseCase;
        this.generatePDFUsecase = generatePDFUsecase;
        this.iSaleRepository = iSaleRepository;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.createSale(sale));
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales(@RequestBody Sale sale){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> viewSale(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(saleUseCase.getSaleById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByIdSale(@PathVariable("id")Long id){
        saleUseCase.getSaleById(id);
        saleUseCase.deleteByIdSale(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSale(@PathVariable("id")Long id, @RequestBody Sale sale){
        saleUseCase.updateSale(id, sale);
        return ResponseEntity.ok("Updated successfully");
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

//            byte[] pdfContent = generatePDFUsecase.generatePDF(sale);
//
//            ByteArrayResource resource = new ByteArrayResource(pdfContent);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sale_" + id + ".pdf");
//
//            return ResponseEntity
//                    .ok()
//                    .headers(headers)
//                    .contentLength(pdfContent.length)
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .body(resource);
    }

}
