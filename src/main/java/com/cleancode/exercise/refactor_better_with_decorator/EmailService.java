package com.cleancode.exercise.refactor_better_with_decorator;

public class EmailService {
    public void sendConfirmation(String customerName) {
        System.out.println("Sending confirmation email to" + customerName + "...");
        // simulate email
    }
}