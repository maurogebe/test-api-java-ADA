package com.example.demo.application.usecases;

import com.example.demo.application.dtos.VersionCompanyDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.VersionCompanyMapper;
import com.example.demo.domain.entities.VersionCompany;
import com.example.demo.domain.repositories.VersionCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VersionCompanyUseCase {

    private final VersionCompanyRepository versionCompanyRepository;

    @Autowired
    public VersionCompanyUseCase(VersionCompanyRepository versionCompanyRepository) {
        this.versionCompanyRepository = versionCompanyRepository;
    }

    public VersionCompanyDTO getVersionCompanyById(Long id) {
        Optional<VersionCompany> versionCompanyById = this.versionCompanyRepository.findById(id);

        if(versionCompanyById.isEmpty()) throw new ApiRequestException("No se encontró la versión de la compañia con ID: " + id, HttpStatus.NOT_FOUND);

        return VersionCompanyMapper.INSTANCE.versionCompanyToVersionCompanyDTO(versionCompanyById.get());
    }
    
    public List<VersionCompanyDTO> getVersionCompanies() {
        List<VersionCompany> versionCompanies = this.versionCompanyRepository.findAll();
        return VersionCompanyMapper.INSTANCE.versionCompaniesToVersionCompaniesDTO(versionCompanies);
    }

    public VersionCompanyDTO createVersionCompany(VersionCompanyDTO versionCompany) {
        VersionCompany versionCompanySaved = this.versionCompanyRepository.save(VersionCompanyMapper.INSTANCE.versionCompanyDTOToVersionCompany(versionCompany));
        
        return VersionCompanyMapper.INSTANCE.versionCompanyToVersionCompanyDTO(versionCompanySaved);
    }
    
    public void updateVersionCompany(Long id, VersionCompanyDTO versionCompany) {
        Optional<VersionCompany> versionCompanyById = this.versionCompanyRepository.findById(id);
        
        if(versionCompanyById.isEmpty()) throw new ApiRequestException("No se encontró la versión de la compañia con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.versionCompanyRepository.save(VersionCompanyMapper.INSTANCE.versionCompanyDTOToVersionCompany(versionCompany));
    }
    
    public void deleteVersionCompany(Long id) {
        Optional<VersionCompany> versionCompanyById = this.versionCompanyRepository.findById(id);
        
        if(versionCompanyById.isEmpty()) throw new ApiRequestException("No se encontró la versión de la compañia con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.versionCompanyRepository.delete(versionCompanyById.get());
    }
}
