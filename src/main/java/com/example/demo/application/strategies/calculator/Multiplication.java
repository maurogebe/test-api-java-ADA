package com.example.demo.application.strategies.calculator;

public class Multiplication implements CalculatorStrategy {
    
    @Override
    public double execute(Double a, Double b) {
        return a * b;
    }
}
