package com.example.demo.domain.entities;

import com.example.demo.application.dtos.AllergyDTO;
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

    @ManyToMany(targetEntity = Allergy.class, fetch = FetchType.EAGER)
    @JoinTable(
        name = "patient_allergies",
        joinColumns = @JoinColumn(name = "allergy_id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private Set<Allergy> allergies;

    public Patient() {
    }

    public void setName(String name) {
        if(name != null) this.name = name;
    }

    public void setEmail(String email) {
        if(email != null) this.email = email;
    }

    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        if(healthInsuranceNumber != null) this.healthInsuranceNumber = healthInsuranceNumber;
    }

    public void setBirthDate(LocalDate birthDate) {
        if(birthDate != null) this.birthDate = birthDate;
    }

    public void setAllergies(Set<Allergy> allergies) {
        if(allergies != null) this.allergies = allergies;
    }
}
