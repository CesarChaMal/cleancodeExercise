package com.cleancode.exercise.refactor_better_functional;

public class UnknowOrder implements Order {
    @Override
    public void process(String customerName) {
        System.out.println("Processing unknow order for " + customerName);
    }
}
