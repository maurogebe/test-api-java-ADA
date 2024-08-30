package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class VersionCompanyDTO {
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("version")
    private VersionDTO version;

}
