package com.example.demo.domain.repositories;

import com.example.demo.domain.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ISaleRepository extends JpaRepository<Sale, Long>{
}
