package com.example.demo.application.strategies.calculator;

public class Division implements CalculatorStrategy {
    
    @Override
    public double execute(Double a, Double b) {
        return a / b;
    }
}
