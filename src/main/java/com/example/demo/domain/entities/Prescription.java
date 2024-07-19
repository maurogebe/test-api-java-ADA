package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@Entity
@Table(name = "Prescription")

public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private long id;
    @Column (name="issueDate_id")
    private LocalDate issueDate;
    @Column (name="DoctorName_id")
    private String DoctorName;

    private Patient patient;
    private List<MedicamentPrescribed> medicamentprescribeds;

    public Prescription() {
    }
}

