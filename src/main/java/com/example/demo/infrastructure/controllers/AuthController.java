package com.example.demo.infrastructure.controllers;

import com.example.demo.application.auth.TokenService;
import com.example.demo.application.usecases.UserUseCase;
import com.example.demo.application.dtos.LoginRequestDTO;
import com.example.demo.application.dtos.LoginResponseDTO;
import com.example.demo.application.dtos.UserResponseDTO;
import com.example.demo.domain.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuarios")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    private UserDetailsService userDetailsService;

    private UserUseCase userUsecase;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UserDetailsService userDetailsService, UserUseCase userUsecase, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
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
            @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        this.userUsecase.createUser(user);
        return ResponseEntity.ok("Usuario creado correctamente");
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y genera un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Email o contraseña inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sign-in")
    public LoginResponseDTO signIn(@RequestBody LoginRequestDTO userLogin) {
        User userByEmail = this.userUsecase.getUserByEmail(userLogin.getEmail());
        if (userByEmail == null || !passwordEncoder.matches(userLogin.getPassword(), userByEmail.getPassword())) {
            throw new RuntimeException("Invalid email or password");
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
