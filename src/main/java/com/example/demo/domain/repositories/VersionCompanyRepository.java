package com.example.demo.domain.repositories;

import com.example.demo.domain.entities.VersionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionCompanyRepository extends JpaRepository<VersionCompany, Long> {

}
