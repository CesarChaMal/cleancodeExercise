package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

public class UnknownOrder implements Order {
    public UnknownOrder(String s) {

    }

    @Override
    public void process(String customerName) {
        System.out.println("Processing unknown order for " + customerName);
    }
}
