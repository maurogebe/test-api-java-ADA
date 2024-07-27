package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MedicamentDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("form")
    private String form;

    @JsonProperty("stock")
    private int stock;

    @JsonProperty("cost")
    private double cost;

    @JsonProperty("prescriptionRequired")
    private boolean prescriptionRequired;

    public void setName(String name) {
        if(name != null) this.name = name;
    }

    public void setDescription(String description) {
        if(description != null) this.description = description;
    }

    public void setForm(String form) {
        if(form != null) this.form = form;
    }

    public void setStock(Integer stock) {
        if(stock != null) this.stock = stock;
    }

    public void setCost(Double cost) {
        if(cost != null) this.cost = cost;
    }

    public void setPrescriptionRequired(Boolean prescriptionRequired) {
        if(prescriptionRequired != null) this.prescriptionRequired = prescriptionRequired;
    }
}
