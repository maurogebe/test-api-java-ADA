package com.example.demo.application.strategies.calculator;

public class Subtraction implements CalculatorStrategy {
    
    @Override
    public double execute(Double a, Double b) {
        return a - b;
    }
}
