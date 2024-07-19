package com.example.demo.domain.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@Entity
@Table(name="MedicamentSold")

public class MedicamentSold {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
@Column (name="id")
private long id;
@Column (name="Medicament_id")
private Medicament medicament;
@Column (name="quantity_id")
private int quantity;
@ManyToOne
private Sale sale;
public MedicamentSold() {
}
}
