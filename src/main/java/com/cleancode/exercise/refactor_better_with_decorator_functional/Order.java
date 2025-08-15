package com.cleancode.exercise.refactor_better_with_decorator_functional;

@FunctionalInterface
public interface Order {
    void process(String customerName);
}
