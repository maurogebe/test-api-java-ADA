package com.example.demo.domain.mappers;

import com.example.demo.domain.dtos.UserResponseDTO;
import com.example.demo.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
        );
    }
}
