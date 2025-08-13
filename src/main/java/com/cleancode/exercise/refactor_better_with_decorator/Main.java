package com.cleancode.exercise.refactor_better_with_decorator;

public class Main {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        OrderProcessor processor = new OrderProcessor();

        Order regular = new RegularOrder();
        Order rush = new RushOrder();
        Order regularWithEmail = new EmailConfirmationOrder(regular, emailService);
        Order rushWithEmail = new EmailConfirmationOrder(rush, emailService);

        processor.process(regular, "Alice");            // No email
        processor.process(regularWithEmail, "Bob");     // With email
        processor.process(rushWithEmail, "Charlie");    // With email
    }
}
