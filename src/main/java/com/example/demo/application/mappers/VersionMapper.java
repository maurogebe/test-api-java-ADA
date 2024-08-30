package com.example.demo.application.mappers;

import com.example.demo.application.dtos.VersionDTO;
import com.example.demo.domain.entities.Version;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VersionMapper {

    VersionMapper INSTANCE = Mappers.getMapper( VersionMapper.class );

    VersionDTO versionToVersionDTO(Version version);

    Version versionDTOToVersion(VersionDTO versionDTO);
    
    List<VersionDTO> versionsToVersionsDTO(List<Version> versions);
    
    List<Version> versionsDTOToVersions(List<VersionDTO> versionsDTO);

}
