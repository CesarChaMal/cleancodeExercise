package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

@FunctionalInterface
public interface RichOrder {
    void process(Customer customer);

    default RichOrder andThen(RichOrder after) {
        return c -> { this.process(c); after.process(c); };
    }
}