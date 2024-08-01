package com.example.demo.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    @NotBlank(message = "The firstName is required.")
    @JsonProperty("firstName")
    private String firstName;
    
    @NotBlank(message = "The lastName is required.")
    @JsonProperty("lastName")
    private String lastName;
    
    @Email(message = "Invalid email format.")
    @NotBlank(message = "The email is required.")
    @JsonProperty("email")
    private String email;
    
    @Size(min = 8, message = "The password must be at least 8 characters long.")
    @JsonProperty("password")
    private String password;

}
