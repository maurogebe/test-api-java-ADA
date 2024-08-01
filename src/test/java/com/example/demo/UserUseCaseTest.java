package com.example.demo;
import com.example.demo.application.dtos.UserDTO;
import com.example.demo.application.dtos.UserResponseDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.UserMapper;
import com.example.demo.application.usecases.UserUseCase;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        // Arrange
        UserDTO user = new UserDTO();
        user.setPassword("plainPassword");
        when(encoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Act
        userUseCase.createUser(user);

        // Assert
        verify(encoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(UserMapper.INSTANCE.userDTOToUser(user));
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void testGetUserById_UserFound() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        UserResponseDTO result = userUseCase.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                userUseCase.getUserById(1L)
        );
        assertEquals("No se encontró el usuario con ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testGetUserByEmail_Success() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        User result = userUseCase.getUserByEmail("user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }
}