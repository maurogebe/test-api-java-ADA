package com.example.demo.domain.entities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "medicament")
public class    Medicament {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "form")
    private String form;

    @Column(name = "stock")
    private int stock;

    @Column(name = "cost")
    private double cost;

    @Column(name = "prescription_required")
    private boolean prescriptionRequired;

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<MedicamentPrescribed> medicamentsPrescribed;

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<MedicamentSold> medicamentsSold;

    public Medicament() {
    }

    public void setName(String name) {
        if(name != null) this.name = name;
    }

    public void setDescription(String description) {
        if(description != null) this.description = description;
    }

    public void setForm(String form) {
        if(form != null) this.form = form;
    }

    public void setStock(Integer stock) {
        if(stock != null) this.stock = stock;
    }

    public void setCost(Double cost) {
        if(cost != null) this.cost = cost;
    }

    public void setPrescriptionRequired(Boolean prescriptionRequired) {
        if(prescriptionRequired != null) this.prescriptionRequired = prescriptionRequired;
    }
}
