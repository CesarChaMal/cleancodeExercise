package com.cleancode.exercise.refactor_better;

public class OrderProcessor {
    private final EmailService emailService;

    public OrderProcessor(EmailService emailService) {
        this.emailService = emailService;
    }

    public void process(Order order, String customerName ) {
        order.process(customerName);
    }

    public void processWithEmail(Order order, String customerName) {
        order.process(customerName);
        emailService.sendConfirmation(customerName);
    }
}
