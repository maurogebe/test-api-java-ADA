package com.example.demo.domain.entities;
import lombok.Data;
@Data
public class MedicamentPrescribed {

    private Long id;
    private Medicament medicament;
    private Prescription prescription;
    private int quantity;
    private String instructions;

    public MedicamentPrescribed(Long id, Medicament medicament, Prescription prescription, int quantity, String instructions) {
        this.id = id;
        this.medicament = medicament;
        this.prescription = prescription;
        this.quantity = quantity;
        this.instructions = instructions;
    }

    public MedicamentPrescribed() {
    }
}
