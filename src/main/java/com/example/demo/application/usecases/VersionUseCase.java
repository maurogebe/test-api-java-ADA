package com.example.demo.application.usecases;

import com.example.demo.application.dtos.VersionDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.VersionMapper;
import com.example.demo.domain.entities.Version;
import com.example.demo.domain.repositories.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VersionUseCase {

    private final VersionRepository versionRepository;

    @Autowired
    public VersionUseCase(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    public VersionDTO getVersionById(Long id) {
        Optional<Version> versionById = this.versionRepository.findById(id);

        if(versionById.isEmpty()) throw new ApiRequestException("No se encontró la versión con ID: " + id, HttpStatus.NOT_FOUND);

        return VersionMapper.INSTANCE.versionToVersionDTO(versionById.get());
    }
    
    public List<VersionDTO> getVersions() {
        List<Version> versions = this.versionRepository.findAll();
        return VersionMapper.INSTANCE.versionsToVersionsDTO(versions);
    }

    public VersionDTO createVersion(VersionDTO version) {
        Version versionSaved = this.versionRepository.save(VersionMapper.INSTANCE.versionDTOToVersion(version));
        return VersionMapper.INSTANCE.versionToVersionDTO(versionSaved);
    }
    
    public void updateVersion(Long id, VersionDTO version) {
        Optional<Version> versionById = this.versionRepository.findById(id);
        
        if(versionById.isEmpty()) throw new ApiRequestException("No se encontró la versión con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.versionRepository.save(VersionMapper.INSTANCE.versionDTOToVersion(version));
    }
    
    public void deleteVersion(Long id) {
        Optional<Version> versionById = this.versionRepository.findById(id);
        
        if(versionById.isEmpty()) throw new ApiRequestException("No se encontró la versión con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.versionRepository.delete(versionById.get());
    }
}
