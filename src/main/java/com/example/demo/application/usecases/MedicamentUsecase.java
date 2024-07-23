package com.example.demo.application.usecases;

import com.example.demo.domain.dtos.MedicamentRequestDTO;
import com.example.demo.domain.dtos.MedicamentResponseDTO;
import com.example.demo.domain.dtos.MedicamentUpdateRequestDTO;
import com.example.demo.domain.entities.Medicament;
import com.example.demo.domain.exeptions.NotFoundException;
import com.example.demo.domain.repositories.MedicamentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentUsecase {

    private final MedicamentRepository medicamentRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MedicamentUsecase(MedicamentRepository medicamentRepository, ObjectMapper objectMapper){
        this.medicamentRepository = medicamentRepository;
        this.objectMapper = objectMapper;
    }

    public MedicamentResponseDTO createMedicament(MedicamentRequestDTO medicamentRequestDTO){
        Medicament medicament = mapToEntity(medicamentRequestDTO);
        this.medicamentRepository.save(medicament);
        return mapToDTO(medicament);
    }

    public List<MedicamentResponseDTO> findAll(){
        return medicamentRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public MedicamentResponseDTO searchMedicament(Long id){
        Medicament medicament = medicamentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("No se encontró medicamento con ID: " + id));
        return mapToDTO(medicament);
    }

    public void deleteById(Long id){
        searchMedicament(id);
        medicamentRepository.deleteById(id);
    }

    public MedicamentResponseDTO updateMedicament(MedicamentUpdateRequestDTO medicamentUpdateRequestDTO){
        searchMedicament(medicamentUpdateRequestDTO.getId());
        Medicament medicament = medicamentRepository.save(mapToEntity(medicamentUpdateRequestDTO));
        return mapToDTO(medicament);
    }

    public MedicamentResponseDTO mapToDTO(Medicament medicament){
        return objectMapper.convertValue(medicament, MedicamentResponseDTO.class);
    }

    public Medicament mapToEntity(MedicamentRequestDTO medicamentRequestDTO){
        return objectMapper.convertValue(medicamentRequestDTO, Medicament.class);
    }

    public Medicament mapToEntity(MedicamentUpdateRequestDTO medicamentUpdateRequestDTO){
        return objectMapper.convertValue(medicamentUpdateRequestDTO, Medicament.class);
    }
}
