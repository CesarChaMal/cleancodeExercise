package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

public class OrderProcessor {
    public void process(Order order, String customerName) {
        order.process(customerName); // that's it!
    }
}
