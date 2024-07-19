package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale")
@NoArgsConstructor
@Setter @Getter
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "total")
    private long total;

    @Column(name = "sale_date")
    private LocalDate saleDate;

    @Column(name = "health_insurance_number")
    private String healthInsuranceNumber;

    @ManyToOne
    @JoinColumn
    private Patient patient;

}
