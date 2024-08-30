package com.example.demo.application.usecases;

import com.example.demo.application.dtos.CompanyDTO;
import com.example.demo.application.dtos.CompanyResponseWithApplicationDTO;
import com.example.demo.application.dtos.CompanyWithVersionDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.CompanyMapper;
import com.example.demo.application.mappers.CompanyWithVersionMapper;
import com.example.demo.domain.entities.Company;
import com.example.demo.domain.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyUseCase {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponseWithApplicationDTO getCompanyByCode(String code) {
        Optional<Company> companyById = this.companyRepository.findByCode(code);

        if(companyById.isEmpty()) throw new ApiRequestException("No se encontró la compañia con codigo: " + code, HttpStatus.NOT_FOUND);

        CompanyWithVersionDTO company = CompanyWithVersionMapper.INSTANCE.companyToCompanyDTO(companyById.get());
        
        return new CompanyResponseWithApplicationDTO(
            company.getCode(),
            company.getName(),
            company.getVersionCompany().getVersion().getApplication().getName(),
            company.getVersionCompany().getVersion().getVersion()
        );
    }
    
    public List<CompanyDTO> getCompanies() {
        List<Company> companies = this.companyRepository.findAll();
        return CompanyMapper.INSTANCE.companiesToCompaniesDTO(companies);
    }

    public CompanyDTO createCompany(CompanyDTO company) {
        Optional<Company> companyByCode = this.companyRepository.findByCode(company.getCode());
        
        if(companyByCode.isPresent()) throw new ApiRequestException("Ya existe una compañia con este codigo: " + company.getCode(), HttpStatus.BAD_REQUEST);
        
        Company companySaved = this.companyRepository.save(CompanyMapper.INSTANCE.companyDTOToCompany(company));
        
        return CompanyMapper.INSTANCE.companyToCompanyDTO(companySaved);
    }
    
    public void updateCompany(Long id, CompanyDTO company) {
        Optional<Company> companyById = this.companyRepository.findById(id);
        
        if(companyById.isEmpty()) throw new ApiRequestException("No se encontró la compañia con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.companyRepository.save(CompanyMapper.INSTANCE.companyDTOToCompany(company));
    }
    
    public void deleteCompany(Long id) {
        Optional<Company> companyById = this.companyRepository.findById(id);
        
        if(companyById.isEmpty()) throw new ApiRequestException("No se encontró la compañia con ID: " + id, HttpStatus.NOT_FOUND);
        
        this.companyRepository.delete(companyById.get());
    }
}
