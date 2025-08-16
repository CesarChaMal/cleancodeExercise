package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

@FunctionalInterface
public interface Order {
    void process(String customerName);

    // Compose two Orders: first this, then 'after'
    default Order andThen(Order after) {
        return customer -> { 
            this.process(customer);
            after.process(customer);
        };
    }

    // Decorate with email using an EmailService
    default Order withEmail(EmailService emailService) {
        return this.andThen(emailService::sendConfirmation);
    }

    // Factory for lambdas
    static Order of(java.util.function.Consumer<String> action) {
        return action::accept;
    }
}