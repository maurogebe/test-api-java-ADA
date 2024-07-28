package com.example.demo.application.usecases;

import com.example.demo.application.dtos.AllergyDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.AllergyMapper;
import com.example.demo.domain.entities.Allergy;
import com.example.demo.domain.repositories.AllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AllergyUseCase {

    private final AllergyRepository allergyRepository;

    @Autowired
    public AllergyUseCase(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    public AllergyDTO createAllergy(AllergyDTO patientDTO){
        Allergy patient = this.allergyRepository.save(AllergyMapper.INSTANCE.allergyDTOToAllergy(patientDTO));
        return AllergyMapper.INSTANCE.allergyToAllergyDTO(patient);
    }

    public Set<AllergyDTO> getAllergies() {
        Set<Allergy> allergies = allergyRepository.findAllAllergies();
        return AllergyMapper.INSTANCE.allergySetToAllergyDTOSet(allergies);
    }

    public AllergyDTO getAllergyById(Long id) {
        Optional<Allergy> patient = allergyRepository.findById(id);
        if(patient.isEmpty()) throw new ApiRequestException("No se encontró paciente con ID: " + id, HttpStatus.NOT_FOUND);
        return AllergyMapper.INSTANCE.allergyToAllergyDTO(patient.get());
    }

    public void deleteById(Long id) {
        getAllergyById(id);
        allergyRepository.deleteById(id);
    }

    public AllergyDTO updateAllergy(Long id, AllergyDTO patientUpdated) {
        AllergyDTO patientById = getAllergyById(id);

        patientById.setName(patientUpdated.getName());
        patientById.setDescription(patientUpdated.getDescription());

        Allergy updatedAllergy = allergyRepository.save(AllergyMapper.INSTANCE.allergyDTOToAllergy(patientById));

        return AllergyMapper.INSTANCE.allergyToAllergyDTO(updatedAllergy);
    }
}
