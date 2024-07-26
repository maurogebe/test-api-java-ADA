package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.SaleUseCase;
import com.example.demo.domain.entities.MedicamentSold;
import com.example.demo.domain.entities.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {

    private SaleUseCase saleUseCase;

    @Autowired
    public SaleController(SaleUseCase saleUseCase) {
        this.saleUseCase = saleUseCase;
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

}
