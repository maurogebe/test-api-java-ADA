package com.example.demo.application.mappers;

import com.example.demo.application.dtos.PrescriptionWithMedicamentDTO;
import com.example.demo.domain.entities.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    PrescriptionMapper INSTANCE = Mappers.getMapper( PrescriptionMapper.class );

    PrescriptionWithMedicamentDTO prescriptionToPrescriptionWithMedicamentDTO(Prescription prescription);

    Prescription prescriptionWithMedicamentDTOToPrescription(PrescriptionWithMedicamentDTO prescription);

    List<PrescriptionWithMedicamentDTO> prescriptionListToprescriptionWithMedicamentDTOList(List<Prescription> prescription);

    List<Prescription> prescriptionWithMedicamentDTOListToPrescriptionList(List<PrescriptionWithMedicamentDTO> prescriptionDTO);

}
