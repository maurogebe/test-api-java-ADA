package com.example.demo.application.usecases;

import com.example.demo.domain.dtos.UserResponseDTO;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.mappers.UserMapper;
import com.example.demo.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserUseCase {

    private UserRepository userRepository;

    private PasswordEncoder encoder;

    private UserMapper userMapper;

    @Autowired
    public UserUseCase(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    public UserResponseDTO getUserById(Long id) throws Exception {
        Optional<UserResponseDTO> userById = this.userRepository
            .findById(id)
            .map(userMapper::toUserResponseDTO);

        if(userById.isEmpty()) throw new Exception("Error");

        return userById.get();
    }

    public void createUser(User user) {
        user.setPassword(this.encoder.encode(user.getPassword()));
        this.userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }
}
