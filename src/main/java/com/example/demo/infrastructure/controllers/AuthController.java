package com.example.demo.infrastructure.controllers;

import com.example.demo.application.auth.TokenService;
import com.example.demo.application.usecases.UserUseCase;
import com.example.demo.domain.dtos.LoginRequestDTO;
import com.example.demo.domain.dtos.LoginResponseDTO;
import com.example.demo.domain.dtos.UserResponseDTO;
import com.example.demo.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam Long id) throws Exception {
        UserResponseDTO user = this.userUsecase.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        this.userUsecase.createUser(user);
        return ResponseEntity.ok("Usuario creado correctamente");
    }

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
