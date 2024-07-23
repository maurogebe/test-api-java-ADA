package com.example.demo.domain.entities;
import lombok.Data;
@Data
public class MedicamentSold {
private Long id;
private Sale sale;
private Medicament medicament;
private int quantity;

public MedicamentSold(Long id, Sale sale, Medicament medicament, int quantity) {
    this.id = id;
    this.sale = sale;
    this.medicament = medicament;
    this.quantity = quantity;
}

public MedicamentSold() {
}
}
