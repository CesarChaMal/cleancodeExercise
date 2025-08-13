package com.cleancode.exercise.refactor_better_functional;

public class RegularOrder implements Order {
    @Override
    public void process(String customerName) {
        System.out.println("Processing regular order for " + customerName);
    }
}
