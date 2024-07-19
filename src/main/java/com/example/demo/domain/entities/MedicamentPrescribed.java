package com.example.demo.domain.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table (name="MedicamentPrescribed")
public class MedicamentPrescribed {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private long id;
    @Column (name="medicament_id")
    private Medicament medicament;
    @Column (name="quaantity_id")
    private int quantity;
    @Column (name="instructions_id")
    private String instructions;
    @OneToMany
    private Prescription prescription;
    public MedicamentPrescribed() {
    }
}
