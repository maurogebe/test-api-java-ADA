package com.example.demo.application.usecases;

import com.example.demo.application.dtos.*;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.MedicamentMapper;
import com.example.demo.application.mappers.PrescriptionMapper;
import com.example.demo.domain.entities.Medicament;
import com.example.demo.domain.entities.Prescription;
import com.example.demo.domain.repositories.PrescriptionRepository;
import lombok.Data;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Service
public class PrescriptionUseCase {

    private PrescriptionRepository prescriptionRepository;
    private MedicamentUseCase medicamentUseCase;
    private TesseractUseCase tesseractUseCase;

    @Autowired
    public PrescriptionUseCase(PrescriptionRepository prescriptionRepository, MedicamentUseCase medicamentUseCase, TesseractUseCase tesseractUseCase) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicamentUseCase = medicamentUseCase;
        this.tesseractUseCase = tesseractUseCase;
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

    public PrescriptionWithMedicamentDTO getPrescriptionWithFile(MultipartFile file) throws Exception {
        if(Objects.requireNonNull(file.getOriginalFilename()).contains("pdf")) {
            File pdfFile = File.createTempFile("uploaded_pdf", ".pdf");
            file.transferTo(pdfFile);
            List<MedicamentFromFileWithOCRDTO> medicaments = tesseractUseCase.extractTextFromPdf(pdfFile);
            pdfFile.delete();

            return getPrescriptionByTextFromFile(medicaments);
        } else {
            List<MedicamentFromFileWithOCRDTO> medicaments = tesseractUseCase.recognizedText(file.getInputStream());
            return getPrescriptionByTextFromFile(medicaments);
        }
    }

    private PrescriptionWithMedicamentDTO getPrescriptionByTextFromFile(List<MedicamentFromFileWithOCRDTO> medicamentsFromFile) {
        List<MedicamentPrescribedWithMedicamentDTO> medicamentsPrescribed = new ArrayList<>();
        for (MedicamentFromFileWithOCRDTO medicamentFromFile : medicamentsFromFile) {
            Optional<Medicament> medicamentByNamePrincipal = medicamentUseCase.getMedicamentByNamePartition(medicamentFromFile.getName());

            medicamentByNamePrincipal.ifPresent(medicament -> medicamentsPrescribed.add(new MedicamentPrescribedWithMedicamentDTO(
                    medicamentFromFile.getDose(),
                    medicamentFromFile.getInstructions(),
                    MedicamentMapper.INSTANCE.medicamentToMedicamentDTO(medicament)
            )));
        }

        PrescriptionWithMedicamentDTO prescription = new PrescriptionWithMedicamentDTO();

        prescription.setMedicamentPrescribeds(medicamentsPrescribed);

        return prescription;

    }

}
