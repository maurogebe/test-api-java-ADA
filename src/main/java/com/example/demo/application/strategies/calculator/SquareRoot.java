package com.example.demo.application.strategies.calculator;

public class SquareRoot implements CalculatorStrategy {
    
    @Override
    public double execute(Double a, Double b) {
        return Math.pow(b, 1 / a);
    }
}
