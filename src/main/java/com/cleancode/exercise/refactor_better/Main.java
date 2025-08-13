package com.cleancode.exercise.refactor_better;

public class Main {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        OrderProcessor processor = new OrderProcessor(emailService);

        Order order = new RegularOrder(); // or new RushOrder(), or new UnknownOrder()

        processor.process(order, "Alice");
        processor.processWithEmail(order, "Bob");
    }
}
