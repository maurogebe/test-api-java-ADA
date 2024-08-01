package com.example.demo.application.usecases;

import com.example.demo.application.dtos.UserDTO;
import com.example.demo.application.dtos.UserResponseDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.UserMapper;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserUseCase {

    private UserRepository userRepository;

    private PasswordEncoder encoder;

    @Autowired
    public UserUseCase(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public UserResponseDTO getUserById(Long id) throws Exception {
        Optional<User> userById = this.userRepository.findById(id);

        if(userById.isEmpty()) throw new ApiRequestException("No se encontró el usuario con ID: " + id, HttpStatus.NOT_FOUND);

        return UserMapper.INSTANCE.userToUserResponseDTO(userById.get());
    }

    public void createUser(UserDTO user) {
        Optional<User> userByEmail = this.userRepository.findByEmail(user.getEmail());
        if(userByEmail.isPresent()) throw new ApiRequestException("Ya existe un usuario con este correo: " + user.getEmail(), HttpStatus.BAD_REQUEST);
        user.setPassword(this.encoder.encode(user.getPassword()));
        this.userRepository.save(UserMapper.INSTANCE.userDTOToUser(user));
    }

    public User getUserByEmail(String email) {
        Optional<User> userByEmail = this.userRepository.findByEmail(email);
        if(userByEmail.isEmpty()) throw new ApiRequestException("No se encontró el usuario con email " + email, HttpStatus.NOT_FOUND);
        return userByEmail.get();
    }
}
