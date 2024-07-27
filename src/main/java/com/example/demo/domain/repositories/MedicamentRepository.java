package com.example.demo.domain.repositories;

import com.example.demo.domain.entities.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    List<Medicament> findByStockLessThanEqual(int stock);
}
