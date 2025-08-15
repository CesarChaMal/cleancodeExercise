// Java
package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features;

import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

public final class AsyncEmailSender implements EmailSender, AutoCloseable {
    private final ExecutorService executor = newVirtualThreadPerTaskExecutor();

    @Override
    public void send(String customerName) {
        // sync send (rarely used here)
        System.out.println("Sending confirmation email to " + customerName + "...");
    }

    public void sendAsync(String customerName) {
        executor.submit(() -> send(customerName));
    }

    @Override
    public void close() {
        executor.close();
    }
}