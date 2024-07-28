package com.example.demo.infrastructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(name = "Estado del Sistema", description = "Operaciones relacionadas con el estado del sistema")
@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Operation(summary = "Obtener el estado del API y la base de datos", description = "Verifica el estado del API y la conexión a la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API y base de datos están en funcionamiento"),
            @ApiResponse(responseCode = "503", description = "La base de datos no está disponible"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<String> getStatus() {
        boolean dbStatus = checkDatabase();

        if (dbStatus) {
            return new ResponseEntity<>("API and Database are up", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Database is down", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private boolean checkDatabase() {
        try {
            // This query can be any simple query to test the connection
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
