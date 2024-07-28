package com.example.demo.domain.repositories;

import com.example.demo.domain.entities.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long>{

    @Query("SELECT a FROM Allergy a")
    Set<Allergy> findAllAllergies();
}
