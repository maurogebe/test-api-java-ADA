package com.example.demo.domain.entities;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
@Data
public class Prescription {

    private Long id;
    private LocalDate issueDate;
    private String String;
    private Patient patient;
    private List<MedicamentPrescribed> medicamentprescribeds;

    public Prescription(Long id, LocalDate issueDate, java.lang.String string, Patient patient, List<MedicamentPrescribed> medicamentprescribeds) {
        this.id = id;
        this.issueDate = issueDate;
        String = string;
        this.patient = patient;
        this.medicamentprescribeds = medicamentprescribeds;
    }

    public Prescription() {
    }

    public void uploadFile() {

    }

    public void createPrescription(){

    }

    public void updatePrescription(){

    }

    public void printPrescription(long id)  {

    }

    public void searchPrescription (long id) {

    }

}

