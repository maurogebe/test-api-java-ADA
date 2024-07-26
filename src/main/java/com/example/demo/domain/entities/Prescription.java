package com.example.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@Entity
@Table(name = "prescription")
@JsonIdentityInfo(scope = Prescription.class, generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
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
    private List<MedicamentPrescribed> medicamentPrescribed;

    public Prescription() {
    }
}

