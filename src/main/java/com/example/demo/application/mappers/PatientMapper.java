package com.example.demo.application.mappers;

import com.example.demo.application.dtos.PatientDTO;
import com.example.demo.domain.entities.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper( PatientMapper.class );

    PatientDTO patientToPatientDTO(Patient patient);

    Patient patientDTOToPatient(PatientDTO patientDTO);

    List<PatientDTO> patientListToPatientDTOList(List<Patient> patient);

    List<Patient> patientDTOListToPatientList(List<PatientDTO> patientDTO);

}
