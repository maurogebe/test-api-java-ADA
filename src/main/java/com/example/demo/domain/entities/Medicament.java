package com.example.demo.domain.entities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "medicament")
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Medicament {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "description")
    private String description;

    @Column (name = "form")
    private String form;

    @Column (name = "stocks")
    private int stocks;

    @Column (name = "cost")
    private long cost;

    @Column (name = "prescriptionRequired")
    private boolean prescriptionRequired;

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<MedicamentPrescribed> medicamentsPrescribed;

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<MedicamentSold> medicamentsSold;

    public Medicament() {
    }
}
