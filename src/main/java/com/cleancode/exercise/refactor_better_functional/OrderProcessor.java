package com.cleancode.exercise.refactor_better_functional;

public class OrderProcessor {
    public void process(Order order, String customerName) {
        order.process(customerName); // that's it!
    }
}
