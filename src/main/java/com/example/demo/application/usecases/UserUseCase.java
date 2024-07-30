package com.example.demo.application.usecases;

import com.example.demo.application.dtos.UserResponseDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.UserMapper;
import com.example.demo.domain.entities.Sale;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

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

    public void createUser(User user) {
        user.setPassword(this.encoder.encode(user.getPassword()));
        this.userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }
}
