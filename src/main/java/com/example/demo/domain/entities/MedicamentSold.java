package com.example.demo.domain.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "MedicamentSold")

public class MedicamentSold {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    public MedicamentSold() {
    }
}
