package com.example.demo.application.mappers;

import com.example.demo.application.dtos.CompanyWithVersionDTO;
import com.example.demo.domain.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyWithVersionMapper {

    CompanyWithVersionMapper INSTANCE = Mappers.getMapper( CompanyWithVersionMapper.class );

    CompanyWithVersionDTO companyToCompanyDTO(Company company);

    Company companyDTOToCompany(CompanyWithVersionDTO companyDTO);
    
    List<CompanyWithVersionDTO> companiesToCompaniesDTO(List<Company> companies);
    
    List<Company> companiesDTOToCompanies(List<CompanyWithVersionDTO> companiesDTO);

}
