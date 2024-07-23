package com.example.demo.domain.entities;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name="last_name")
    private String lastName;
    private String email;

    @Column(name="health_insurance_number")
    private String healthInsuranceNumber;

    @Column(name="birth_date")
    private LocalDate birthDate;

    @OneToMany(targetEntity = Allergy.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Allergy> allergies;
}
