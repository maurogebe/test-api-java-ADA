package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicamentSoldWithMedicamentDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("medicament")
    private MedicamentDTO medicament;
}
