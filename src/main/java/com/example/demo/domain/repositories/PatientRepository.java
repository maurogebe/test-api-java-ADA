package com.example.demo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
