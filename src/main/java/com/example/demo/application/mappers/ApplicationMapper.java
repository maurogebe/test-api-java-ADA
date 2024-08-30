package com.example.demo.application.mappers;

import com.example.demo.application.dtos.ApplicationDTO;
import com.example.demo.domain.entities.Application;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationMapper INSTANCE = Mappers.getMapper( ApplicationMapper.class );

    ApplicationDTO applicationToApplicationDTO(Application application);

    Application applicationDTOToApplication(ApplicationDTO applicationDTO);
    
    List<ApplicationDTO> applicationsToApplicationsDTO(List<Application> applications);
    
    List<Application> applicationsDTOToApplications(List<ApplicationDTO> applicationsDTO);

}
