package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class PatientDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("healthInsuranceNumber")
    private String healthInsuranceNumber;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    @JsonProperty("allergies")
    private Set<AllergyDTO> allergies;

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

    public void setAllergies(Set<AllergyDTO> allergies) {
        if(allergies != null) this.allergies = allergies;
    }
}
