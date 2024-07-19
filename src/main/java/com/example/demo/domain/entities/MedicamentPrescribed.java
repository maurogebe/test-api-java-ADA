package com.example.demo.domain.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table (name = "medicament_prescribed")
public class MedicamentPrescribed {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private long id;

    @Column (name = "quantity")
    private int quantity;

    @Column (name = "instructions")
    private String instructions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "medicament_id")
    private Medicament medicament;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "prescription_id")
    private Prescription prescription;

    public MedicamentPrescribed() {
    }
}
