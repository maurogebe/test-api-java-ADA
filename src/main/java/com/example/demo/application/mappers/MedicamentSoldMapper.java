package com.example.demo.application.mappers;

import com.example.demo.application.dtos.MedicamentSoldWithMedicamentDTO;
import com.example.demo.domain.entities.MedicamentSold;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicamentSoldMapper {

    MedicamentSoldMapper INSTANCE = Mappers.getMapper( MedicamentSoldMapper.class );

    MedicamentSoldWithMedicamentDTO medicamentSoldToMedicamentSoldWithMedicamentDTO(MedicamentSold medicamentSold);

    MedicamentSold medicamentSoldWithMedicamentDTOToMedicamentSold(MedicamentSoldWithMedicamentDTO medicamentSoldDTO);

    List<MedicamentSoldWithMedicamentDTO> medicamentSoldListToMedicamentSoldWithMedicamentDTOList(List<MedicamentSold> medicamentSold);

    List<MedicamentSold> medicamentSoldWithMedicamentDTOListToMedicamentSoldList(List<MedicamentSoldWithMedicamentDTO> medicamentSoldDTO);

}
