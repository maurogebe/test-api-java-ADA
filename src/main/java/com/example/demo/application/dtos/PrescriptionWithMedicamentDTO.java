package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PrescriptionWithMedicamentDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("issueDate")
    private LocalDate issueDate;

    @JsonProperty("doctorName")
    private String doctorName;

    @JsonProperty("patient")
    private PatientDTO patient;

    @JsonProperty("medicamentPrescribeds")
    private List<MedicamentPrescribedWithMedicamentDTO> medicamentPrescribeds;

    public void setIssueDate(LocalDate issueDate) {
        if(issueDate != null) this.issueDate = issueDate;
    }

    public void setDoctorName(String doctorName) {
        if(doctorName != null) this.doctorName = doctorName;
    }

    public void setPatient(PatientDTO patient) {
        if(patient != null) this.patient = patient;
    }

    public void setMedicamentPrescribeds(List<MedicamentPrescribedWithMedicamentDTO> medicamentPrescribeds) {
        if(medicamentPrescribeds != null) this.medicamentPrescribeds = medicamentPrescribeds;
    }
}
