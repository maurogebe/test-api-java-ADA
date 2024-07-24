package com.example.demo.application.usecases;

import com.example.demo.domain.entities.Prescription;
import com.example.demo.domain.repositories.IPrescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Data
@Service
public class PrescriptionUseCase {

    private IPrescription iprescription;

    @Autowired
    private final ObjectMapper objectMapper;

    public PrescriptionUseCase(ObjectMapper objectMapper, IPrescription iprescription) {
        this.objectMapper = objectMapper;
        this.iprescription = iprescription;
    }

    public Prescription createPrescription(Prescription prescription){
        this.iprescription.save(prescription);
        return (prescription);
    }

    public Prescription fintByIDPrescription (Long id){
        Prescription prescription = iprescription.findById(id).get();
        return  (prescription);
    }

    public void deleteByIdPrescription(Long id) {
        fintByIdPrescription(id);
        iprescription.deleteById(id);
    }
    public Prescription fintByIdPrescription (Long id) {
        Prescription prescription = iprescription.findById(id).get();
        return (prescription);
    }

    public void updatePrescription (Long id, Prescription prescription) {
        Prescription prescriptionUpdate = iprescription.findById(id).get();
        prescriptionUpdate.setDoctorName(prescription.getDoctorName());
        prescriptionUpdate.setIssueDate(prescription.getIssueDate());
        prescriptionUpdate.setPatient(prescription.getPatient());
        prescriptionUpdate.setMedicamentPrescribed(prescription.getMedicamentPrescribed());
        iprescription.save(prescriptionUpdate);

    }

}
