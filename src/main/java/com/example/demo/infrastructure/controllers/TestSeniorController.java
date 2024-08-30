package com.example.demo.infrastructure.controllers;

import com.example.demo.application.dtos.CalculatorRequestDTO;
import com.example.demo.application.usecases.TestSeniorUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/senior")
public class TestSeniorController {

    private final TestSeniorUseCase testSeniorUseCase;

    @Autowired
    public TestSeniorController(TestSeniorUseCase testSeniorUseCase) {
        this.testSeniorUseCase = testSeniorUseCase;
    }

    @PostMapping("/calculator")
    public ResponseEntity<Double> calculator(@RequestBody CalculatorRequestDTO operation) {
        Double result = this.testSeniorUseCase.calculator(operation.getOperation());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    
    @PostMapping("/generate-words")
    public ResponseEntity<String> generateWords(@RequestBody String[] letters) {
        String word = this.testSeniorUseCase.generateWords(letters);
        return ResponseEntity.status(HttpStatus.CREATED).body(word);
    }
}
