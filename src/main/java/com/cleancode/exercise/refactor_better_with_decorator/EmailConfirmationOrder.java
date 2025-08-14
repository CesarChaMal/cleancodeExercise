package com.cleancode.exercise.refactor_better_with_decorator;

public class EmailConfirmationOrder implements Order {
    private final Order order;
    private final EmailService emailService;

    public EmailConfirmationOrder(Order order, EmailService emailService) {
        this.order = order;
        this.emailService = emailService;
    }

    @Override
    public void process(String customerName) {
        order.process(customerName);                         // delegate original behavior
        emailService.sendConfirmation(customerName);           // add new behavior
    }
}
