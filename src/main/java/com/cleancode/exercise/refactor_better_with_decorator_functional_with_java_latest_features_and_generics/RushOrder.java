package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

public class RushOrder implements Order {
    public RushOrder(String s) {
    }

    @Override
    public void process(String customerName) {
        System.out.println("Processing rush order order for " + customerName);
    }
}
