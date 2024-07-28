package com.example.demo.application.mappers;

import com.example.demo.application.dtos.AllergyDTO;
import com.example.demo.domain.entities.Allergy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AllergyMapper {

    AllergyMapper INSTANCE = Mappers.getMapper( AllergyMapper.class );

    AllergyDTO allergyToAllergyDTO(Allergy allergy);

    Allergy allergyDTOToAllergy(AllergyDTO allergyDTO);

    Set<AllergyDTO> allergySetToAllergyDTOSet(Set<Allergy> allergy);

    Set<Allergy> allergyDTOSetToAllergySet(List<Allergy> allergyDTO);

}
