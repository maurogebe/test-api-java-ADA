package com.example.demo.application.usecases;

import com.example.demo.application.dtos.PatientDTO;
import com.example.demo.application.mappers.PatientMapper;
import com.example.demo.domain.entities.Patient;
import com.example.demo.domain.exeptions.NotFoundException;
import com.example.demo.domain.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientUseCase {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDTO createPatient(PatientDTO patientDTO){
        Patient patient = this.patientRepository.save(PatientMapper.INSTANCE.patientDTOToPatient(patientDTO));
        return PatientMapper.INSTANCE.patientToPatientDTO(patient);
    }

    public List<PatientDTO> getPatients() {
        List<Patient> patientList = patientRepository.findAll();
        return PatientMapper.INSTANCE.patientListToPatientDTOList(patientList);
    }

    public PatientDTO getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()) new NotFoundException("No se encontró paciente con ID: " + id);
        return PatientMapper.INSTANCE.patientToPatientDTO(patient.get());
    }

    public void deleteById(Long id) {
        getPatientById(id);
        patientRepository.deleteById(id);
    }

    public PatientDTO updatePatient(Long id, PatientDTO patientUpdated) {
        PatientDTO patientById = getPatientById(id);

        patientById.setName(patientUpdated.getName());
        patientById.setEmail(patientUpdated.getEmail());
        patientById.setHealthInsuranceNumber(patientUpdated.getHealthInsuranceNumber());
        patientById.setBirthDate(patientUpdated.getBirthDate());
        patientById.setAllergies(patientUpdated.getAllergies());

        Patient updatedPatient = patientRepository.save(PatientMapper.INSTANCE.patientDTOToPatient(patientById));

        return PatientMapper.INSTANCE.patientToPatientDTO(updatedPatient);
    }
}
