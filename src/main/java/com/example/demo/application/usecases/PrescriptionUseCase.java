package com.example.demo.application.usecases;

import com.example.demo.application.dtos.PrescriptionWithMedicamentDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.PrescriptionMapper;
import com.example.demo.domain.entities.Prescription;
import com.example.demo.domain.repositories.PrescriptionRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class PrescriptionUseCase {

    private PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionUseCase(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public PrescriptionWithMedicamentDTO createPrescription(PrescriptionWithMedicamentDTO prescriptionDTO){
        Prescription prescription = this.prescriptionRepository.save(PrescriptionMapper.INSTANCE.prescriptionWithMedicamentDTOToPrescription(prescriptionDTO));
        return PrescriptionMapper.INSTANCE.prescriptionToPrescriptionWithMedicamentDTO(prescription);
    }

    public PrescriptionWithMedicamentDTO getPrescriptionById (Long id){
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        if(prescription.isEmpty()) throw new ApiRequestException("No se encontró la prescripción con ID: " + id, HttpStatus.NOT_FOUND);
        return PrescriptionMapper.INSTANCE.prescriptionToPrescriptionWithMedicamentDTO(prescription.get());
    }

    public void deletePrescriptionById(Long id) {
        getPrescriptionById(id);
        prescriptionRepository.deleteById(id);
    }

    public PrescriptionWithMedicamentDTO updatePrescription (Long id, PrescriptionWithMedicamentDTO prescriptionUpdate) {
        PrescriptionWithMedicamentDTO prescriptionById = getPrescriptionById(id);

        prescriptionById.setDoctorName(prescriptionUpdate.getDoctorName());
        prescriptionById.setIssueDate(prescriptionUpdate.getIssueDate());
        prescriptionById.setPatient(prescriptionUpdate.getPatient());
        prescriptionById.setMedicamentPrescribeds(prescriptionUpdate.getMedicamentPrescribeds());

        Prescription prescriptionUpdated = prescriptionRepository.save(PrescriptionMapper.INSTANCE.prescriptionWithMedicamentDTOToPrescription(prescriptionUpdate));

        return PrescriptionMapper.INSTANCE.prescriptionToPrescriptionWithMedicamentDTO(prescriptionUpdated);
    }

}
