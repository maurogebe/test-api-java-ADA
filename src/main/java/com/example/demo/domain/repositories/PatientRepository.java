package com.example.demo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.entities.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
