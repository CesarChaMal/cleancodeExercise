package com.cleancode.exercise.refactor_better_with_decorator;

public class EmailConfirmationOrder implements Order {
    private final Order wrapped;
    private final EmailService emailService;

    public EmailConfirmationOrder(Order wrapped, EmailService emailService) {
        this.wrapped = wrapped;
        this.emailService = emailService;
    }

    @Override
    public void process(String customerName) {
        wrapped.process(customerName);                         // delegate original behavior
        emailService.sendConfirmation(customerName);           // add new behavior
    }
}
