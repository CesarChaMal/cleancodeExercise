package com.cleancode.exercise.reafactor_better_functional;

@FunctionalInterface
public interface Order {
    void process(String customerName);
}
