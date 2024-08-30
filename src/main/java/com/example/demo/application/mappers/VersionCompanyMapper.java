package com.example.demo.application.mappers;

import com.example.demo.application.dtos.VersionCompanyDTO;
import com.example.demo.domain.entities.VersionCompany;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VersionCompanyMapper {

    VersionCompanyMapper INSTANCE = Mappers.getMapper( VersionCompanyMapper.class );

    VersionCompanyDTO versionCompanyToVersionCompanyDTO(VersionCompany versionCompany);

    VersionCompany versionCompanyDTOToVersionCompany(VersionCompanyDTO versionCompanyDTO);
    
    List<VersionCompanyDTO> versionCompaniesToVersionCompaniesDTO(List<VersionCompany> versionCompanies);
    
    List<VersionCompany> versionCompaniesDTOToVersionCompanies(List<VersionCompanyDTO> versionCompaniesDTO);

}
