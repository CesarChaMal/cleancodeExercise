package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

// Java
public class EmailService {
    private final java.util.concurrent.ExecutorService executor =
        newVirtualThreadPerTaskExecutor();

    public void sendConfirmation(String customerName) {
        System.out.println("Sending confirmation email to " + customerName + "...");
        // simulate work
    }

    public void sendConfirmationAsync(String customerName) {
        executor.submit(() -> sendConfirmation(customerName));
    }
}