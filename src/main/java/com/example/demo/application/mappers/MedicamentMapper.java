package com.example.demo.application.mappers;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.domain.entities.Medicament;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {

    MedicamentMapper INSTANCE = Mappers.getMapper( MedicamentMapper.class );

    MedicamentDTO medicamentToMedicamentDTO(Medicament medicament);

    Medicament medicamentDTOToMedicament(MedicamentDTO medicamentDTO);

    List<MedicamentDTO> medicamentListToMedicamentDTOList(List<Medicament> medicament);

    List<Medicament> medicamentListDTOToMedicamentList(List<MedicamentDTO> medicamentDTO);

}
