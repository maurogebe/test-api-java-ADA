package com.example.demo.domain.repositories;

import com.example.demo.domain.entities.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    List<Medicament> findByStockLessThanEqual(int stock);

    Optional<Medicament> findByName(@Param("name") String name);

    List<Medicament> findByNameContainingIgnoreCase(String name);
}
