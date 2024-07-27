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
    private int instructions;

    @JsonProperty("medicament")
    private MedicamentDTO medicament;
}
