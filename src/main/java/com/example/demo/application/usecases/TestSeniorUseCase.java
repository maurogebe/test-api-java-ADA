package com.example.demo.application.usecases;

import com.example.demo.application.strategies.calculator.*;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.*;

@Service
public class TestSeniorUseCase {
    
    private CalculatorStrategy calculatorStrategy;

    public Double calculator(String operation) {
        String[] operationArr = operation.split(" ");
        Optional<Double> a = null;
        Optional<Double> b = null;
        for (String s : operationArr) {
            try {
                double number = Double.parseDouble(s);
                if(a == null) {
                    a = Optional.of(number);
                } else {
                    b = Optional.of(number);
                }
            } catch (NumberFormatException ignored) {}
            switch (s.toUpperCase()) {
                case "+" -> this.calculatorStrategy = new Addition();
                case "-" -> this.calculatorStrategy = new Subtraction();
                case "X" -> this.calculatorStrategy = new Multiplication();
                case "/" -> this.calculatorStrategy = new Division();
                case "√" -> this.calculatorStrategy = new SquareRoot();
            }
        }
        
        return this.calculatorStrategy.execute(a.get(), b.get());
    }
    
    public String generateWords(String[] letters) {
        StringBuilder word = new StringBuilder();
        String[] vocales = new String[]{"a", "e", "i", "o", "u"};
        for (String letter : letters) {
            String[] wordSplit = word.toString().split("");
            if(wordSplit.length > 10) break;
            Optional<String> existLastVocal = Arrays.stream(vocales)
                .filter(v -> v.equals(wordSplit[wordSplit.length - 1]))
                .findFirst();
            
            Optional<String> equalLetterToVocal = Arrays.stream(vocales)
                .filter(v -> v.equals(letter))
                .findFirst();
            if(existLastVocal.isEmpty() && equalLetterToVocal.isPresent()) {
                word.append(letter);
            } else if(existLastVocal.isPresent() && equalLetterToVocal.isEmpty()) {
                word.append(letter);
            }
        }
        return word.toString();
    }
}
