package com.example.demo.domain.entities;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
@Data
public class Sale {

    private Long id;
    private Patient Patient;
    private Double total;
    private LocalDate saleDate;
    private List <MedicamentPrescribed> medicamentprescribeds;

    public Sale(Long id, Patient patient, Double total, LocalDate saleDate, List<MedicamentPrescribed> medicamentprescribeds) {
        this.id = id;
        Patient = patient;
        this.total = total;
        this.saleDate = saleDate;
        this.medicamentprescribeds = medicamentprescribeds;
    }

    public Sale() {
    }

    public void generateAndSendSaleReceipt() {

    }

    public void viewSale () {

    }
}
