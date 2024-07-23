package com.example.demo.application.usecases;

import com.example.demo.domain.dtos.PatientRequestDTO;
import com.example.demo.domain.dtos.PatientResponseDTO;
import com.example.demo.domain.dtos.PatientUpdateRequestDTO;
import com.example.demo.domain.entities.Allergy;
import com.example.demo.domain.entities.Patient;
import com.example.demo.domain.exeptions.NotFoundException;
import com.example.demo.domain.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientUsecase {

    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PatientUsecase(PatientRepository patientRepository, ObjectMapper objectMapper) {
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        Patient patient = mapToEntity(patientRequestDTO);
        this.patientRepository.save(patient);
        return mapToDTO(patient);
    }

    public List<PatientResponseDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public PatientResponseDTO searchPatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new NotFoundException("No se encontró paciente con ID: " + id));
        return mapToDTO(patient);
    }

    public void deleteById(Long id) {
        searchPatient(id);
        patientRepository.deleteById(id);
    }

    public PatientResponseDTO updatePatient(PatientUpdateRequestDTO patientUpdateRequestDTO) {
        Patient existingPatient = patientRepository.findById(patientUpdateRequestDTO.getId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));

        // Actualizar los campos del paciente existente con los datos del DTO
        existingPatient.setName(patientUpdateRequestDTO.getName());
        existingPatient.setLastName(patientUpdateRequestDTO.getLastName());
        existingPatient.setEmail(patientUpdateRequestDTO.getEmail());
        existingPatient.setHealthInsuranceNumber(patientUpdateRequestDTO.getHealthInsuranceNumber());
        existingPatient.setBirthDate(patientUpdateRequestDTO.getBirthDate());

        List<Allergy> updatedAllergies = patientUpdateRequestDTO.getAllergies();
        existingPatient.setAllergies(updatedAllergies);  // Aquí asumiendo que allergies es una lista de alergias actualizada

        Patient updatedPatient = patientRepository.save(existingPatient);

        return mapToDTO(updatedPatient);
    }

    private PatientResponseDTO mapToDTO(Patient patient){
        return objectMapper.convertValue(patient, PatientResponseDTO.class);
    }

    private Patient mapToEntity(PatientRequestDTO patientRequestDTO){
        return objectMapper.convertValue(patientRequestDTO, Patient.class);
    }
}
