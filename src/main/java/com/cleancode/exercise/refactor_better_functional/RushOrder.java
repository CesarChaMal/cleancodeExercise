package com.cleancode.exercise.refactor_better_functional;

public class RushOrder implements Order {
    @Override
    public void process(String customerName) {
        System.out.println("Processing rush order order for " + customerName);
    }
}
