package com.example.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Entity
@Table(name = "patient")
//@JsonIdentityInfo(scope = Patient.class, generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Patient {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "email")
    private String email;

    @Column (name = "health_insurance_number")
    private String healthInsuranceNumber;

    @Column (name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "patient")
    private List<Sale> sale;

    @OneToMany(mappedBy = "patient")
    private List<Prescription> prescription;

    @ManyToMany
    @JoinTable(
        name = "patient_allergies",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "allergy_id")
    )
    private Set<Allergy> allergies;

    public Patient() {
    }
}
