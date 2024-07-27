package com.example.demo.domain.entities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "medicament_sold")
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

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    public MedicamentSold() {
    }
}
