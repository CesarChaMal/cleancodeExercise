package com.cleancode.exercise.refactor_better_functional;

@FunctionalInterface
public interface Order {
    void process(String customerName);
}
