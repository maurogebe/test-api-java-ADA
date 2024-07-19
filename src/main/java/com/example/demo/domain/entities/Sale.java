package com.example.demo.domain.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@Entity
@Table(name = "Sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private long id;
    @Column (name="total_id")
    private Double total;
    @Column (name="SaleDate_id")
    private LocalDate SaleDate;

    private List <MedicamentPrescribed> medicamentprescribeds;


    public Sale() {
    }
}
