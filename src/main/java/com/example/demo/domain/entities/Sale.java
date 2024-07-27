package com.example.demo.domain.entities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private long id;

    @Column (name = "total")
    private Double total;

    @Column (name = "sale_date")
    private LocalDate saleDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MedicamentSold> medicamentsSold = new ArrayList<>();

    public Sale() {
    }

    public void addMedicamentSold(MedicamentSold medicamentSold) {
        this.medicamentsSold.add(medicamentSold);
        medicamentSold.setSale(this);
    }

    public void setMedicamentsSold(List<MedicamentSold> medicamentsSold) {
        if(this.medicamentsSold == null) {
            this.medicamentsSold = medicamentsSold;
        } else {
            for (MedicamentSold medicamentSold : medicamentsSold) {
                if (medicamentSold.getId() >= 0){
                    OptionalInt indexOpt = IntStream.range(0, medicamentsSold.size())
                            .filter(i -> medicamentsSold.get(i).getId() == medicamentSold.getId())
                            .findFirst();
                    int index = indexOpt.orElse(-1);
                    if (index >= 0) this.medicamentsSold.add(index, medicamentSold);
                }  else {
                    this.medicamentsSold.add(medicamentSold);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Sale{id=" + id + ", total=" + total + ", saleDate=" + saleDate + "}";
    }
}
