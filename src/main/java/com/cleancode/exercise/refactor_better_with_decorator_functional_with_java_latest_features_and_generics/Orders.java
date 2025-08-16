package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

public final class Orders {
    private Orders() {}

    public static Order timed(Order order, java.io.PrintStream out) {
        return customer -> {
            long start = System.nanoTime();
            order.process(customer);
            long end = System.nanoTime();
            out.printf("Processed %s in %d μs%n", customer, (end - start) / 1_000);
        };
    }

    public static Order logged(Order order, java.io.PrintStream out, String label) {
        return customer -> {
            out.printf("[%s] -> %s%n", label, customer);
            order.process(customer);
            out.printf("[%s] <- %s%n", label, customer);
        };
    }

    public static Order retried(Order order, int attempts) {
        return customer -> {
            RuntimeException last = null;
            for (int i = 0; i < attempts; i++) {
                try {
                    order.process(customer);
                    return;
                } catch (RuntimeException e) {
                    last = e;
                }
            }
            if (last != null) throw last;
        };
    }
}