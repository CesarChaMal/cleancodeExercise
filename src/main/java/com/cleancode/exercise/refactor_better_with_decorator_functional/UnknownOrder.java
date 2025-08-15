package com.cleancode.exercise.refactor_better_with_decorator_functional;

public class UnknownOrder implements Order {
    @Override
    public void process(String customerName) {
        System.out.println("Processing unknown order for " + customerName);
    }
}
