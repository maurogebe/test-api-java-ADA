package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SaleWithMedicamentDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("total")
    private double total;

    @JsonProperty("saleDate")
    private LocalDate saleDate;

    @JsonProperty("patient")
    private PatientDTO patient;

    @JsonProperty("medicamentsSold")
    private List<MedicamentSoldWithMedicamentDTO> medicamentsSold;

    public void setTotal(Double total) {
        if(total != null) this.total = total;
    }

    public void setSaleDate(LocalDate saleDate) {
        if(saleDate != null) this.saleDate = saleDate;
    }

    public void setPatient(PatientDTO patient) {
        if(patient != null) this.patient = patient;
    }

    public void setMedicamentsSold(List<MedicamentSoldWithMedicamentDTO> medicamentsSold) {
        if(medicamentsSold != null) this.medicamentsSold = medicamentsSold;
    }
}
