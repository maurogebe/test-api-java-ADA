package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicamentPrescribedWithMedicamentDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("instructions")
    private String instructions;

    @JsonProperty("medicament")
    private MedicamentDTO medicament;

    public MedicamentPrescribedWithMedicamentDTO(int quantity, String instructions, MedicamentDTO medicament) {
        this.quantity = quantity;
        this.instructions = instructions;
        this.medicament = medicament;
    }
}
