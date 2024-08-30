package com.example.demo.application.mappers;

import com.example.demo.application.dtos.CompanyDTO;
import com.example.demo.domain.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper( CompanyMapper.class );

    CompanyDTO companyToCompanyDTO(Company company);

    Company companyDTOToCompany(CompanyDTO companyDTO);
    
    List<CompanyDTO> companiesToCompaniesDTO(List<Company> companies);
    
    List<Company> companiesDTOToCompanies(List<CompanyDTO> companiesDTO);

}
