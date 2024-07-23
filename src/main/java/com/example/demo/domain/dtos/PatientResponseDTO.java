package com.example.demo.domain.dtos;

import com.example.demo.domain.entities.Allergy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String healthInsuranceNumber;
    private LocalDate birthDate;
    private List<Allergy> allergies;
}
