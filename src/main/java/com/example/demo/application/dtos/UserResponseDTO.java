package com.example.demo.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String LastName;
    private String email;
}
