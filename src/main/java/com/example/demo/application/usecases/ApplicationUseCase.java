package com.example.demo.application.usecases;

import com.example.demo.application.dtos.ApplicationDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.ApplicationMapper;
import com.example.demo.domain.entities.Application;
import com.example.demo.domain.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationUseCase {

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationUseCase(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public ApplicationDTO getApplicationById(Long id) {
        Optional<Application> applicationById = this.applicationRepository.findById(id);

        if(applicationById.isEmpty()) throw new ApiRequestException("No se encontró la aplicación con ID: " + id, HttpStatus.NOT_FOUND);

        return ApplicationMapper.INSTANCE.applicationToApplicationDTO(applicationById.get());
    }
    
    public List<ApplicationDTO> getApplications() {
        List<Application> applications = this.applicationRepository.findAll();
        return ApplicationMapper.INSTANCE.applicationsToApplicationsDTO(applications);
    }

    public ApplicationDTO createApplication(ApplicationDTO application) {
        Optional<Application> applicationByCode = this.applicationRepository.findByCode(application.getCode());
        
        if(applicationByCode.isPresent()) throw new ApiRequestException("Ya existe una aplicación con este codigo: " + application.getCode(), HttpStatus.BAD_REQUEST);
        
        Application applicationSaved = this.applicationRepository.save(ApplicationMapper.INSTANCE.applicationDTOToApplication(application));
        
        return ApplicationMapper.INSTANCE.applicationToApplicationDTO(applicationSaved);
    }
    
    public void updateApplication(Long id, ApplicationDTO application) {
        Optional<Application> applicationById = this.applicationRepository.findById(id);
        
        if(applicationById.isEmpty()) throw new ApiRequestException("No se encontró la aplicación con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.applicationRepository.save(ApplicationMapper.INSTANCE.applicationDTOToApplication(application));
    }
    
    public void deleteApplication(Long id) {
        Optional<Application> applicationById = this.applicationRepository.findById(id);
        
        if(applicationById.isEmpty()) throw new ApiRequestException("No se encontró la aplicación con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.applicationRepository.delete(applicationById.get());
    }
}
