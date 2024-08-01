package com.example.demo.infrastructure.controllers;

import com.example.demo.application.auth.TokenService;
import com.example.demo.application.dtos.UserDTO;
import com.example.demo.application.dtos.UserResponseDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.usecases.UserUseCase;
import com.example.demo.application.dtos.LoginRequestDTO;
import com.example.demo.application.dtos.LoginResponseDTO;
import com.example.demo.domain.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuarios")
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UserUseCase userUsecase;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UserUseCase userUsecase, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userUsecase = userUsecase;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Obtener información de usuario por ID", description = "Retorna la información de un usuario dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam Long id) throws Exception {
        UserResponseDTO user = this.userUsecase.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO user) {
        this.userUsecase.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y genera un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
        @ApiResponse(responseCode = "401", description = "Email o contraseña inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sign-in")
    public LoginResponseDTO signIn(@Valid @RequestBody LoginRequestDTO userLogin) {
        User userByEmail = this.userUsecase.getUserByEmail(userLogin.getEmail());
        if (userByEmail == null || !passwordEncoder.matches(userLogin.getPassword(), userByEmail.getPassword())) {
            throw new ApiRequestException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userByEmail.getId(), userLogin.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new LoginResponseDTO(
            userByEmail.getId(),
            userByEmail.getFirstName(),
            userByEmail.getLastName(),
            userByEmail.getEmail(),
            tokenService.generateToken(userDetails.getUsername())
        );
    }
}
