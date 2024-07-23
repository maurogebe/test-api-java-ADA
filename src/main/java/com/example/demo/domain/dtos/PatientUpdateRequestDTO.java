package com.example.demo.domain.dtos;

import com.example.demo.domain.entities.Allergy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientUpdateRequestDTO {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String healthInsuranceNumber;
    private LocalDate birthDate;
    private List<Allergy> allergies;
}
