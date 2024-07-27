package com.example.demo.application.mappers;

import com.example.demo.application.dtos.MedicamentPrescribedWithMedicamentDTO;
import com.example.demo.domain.entities.MedicamentPrescribed;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicamentPrescribedMapper {

    MedicamentPrescribedMapper INSTANCE = Mappers.getMapper( MedicamentPrescribedMapper.class );

    MedicamentPrescribedWithMedicamentDTO medicamentPrescribedToMedicamentPrescribedWithMedicamentDTO(MedicamentPrescribed medicamentPrescribed);

    MedicamentPrescribed medicamentPrescribedWithMedicamentDTOToMedicamentPrescribed(MedicamentPrescribedWithMedicamentDTO medicamentPrescribedDTO);

    List<MedicamentPrescribedWithMedicamentDTO> medicamentPrescribedListToMedicamentPrescribedWithMedicamentDTOList(List<MedicamentPrescribed> medicamentPrescribed);

    List<MedicamentPrescribed> medicamentPrescribedWithMedicamentDTOListToMedicamentPrescribedList(List<MedicamentPrescribedWithMedicamentDTO> medicamentPrescribedDTO);

}
