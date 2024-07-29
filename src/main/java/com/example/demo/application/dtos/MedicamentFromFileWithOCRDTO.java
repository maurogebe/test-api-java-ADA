package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MedicamentFromFileWithOCRDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("dose")
    private int dose;

    @JsonProperty("form")
    private String form;

    @JsonProperty("instructions")
    private String instructions;
}
