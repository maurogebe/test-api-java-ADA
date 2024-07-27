package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private long id;

    @Column (name = "issue_date")
    private LocalDate issueDate;

    @Column (name = "doctor_name")
    private String doctorName;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    private List<MedicamentPrescribed> medicamentPrescribeds;

    public Prescription() {
    }
}

