package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@Entity
@Table(name="Patient")

public class Patient {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private long id;
    @Column (name="name_id")
    private String name;
    @Column (name="email_id")
    private String email;
    @Column (name="healthInsuranceNumber_id")
    private String healthInsuranceNumber;
    @Column (name="birthDate_id")
    private LocalDate birthDate;
    @OneToMany
    private Sale sale;
    @OneToMany
    private Prescription prescription;
    public Patient() {
    }
}
