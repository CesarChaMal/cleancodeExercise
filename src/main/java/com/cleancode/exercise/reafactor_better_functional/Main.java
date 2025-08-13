package com.cleancode.exercise.reafactor_better_functional;

public class Main {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();

        // === OOP-based usage ===
        Order regular = new RegularOrder();
        Order rush = new RushOrder();

        // Compose with decorator
        Order regularWithEmail = new EmailConfirmationOrder(regular, emailService);
        Order rushWithEmail = new EmailConfirmationOrder(rush, emailService);

        regular.process("Alice");            // No email
        rushWithEmail.process("Bob");        // With email

        // === Functional-style usage (via lambdas) ===
        Order functionalRegular = customer -> System.out.println("Processing regular order for " + customer);
        Order functionalRush = customer -> System.out.println("Processing rush order for " + customer);

        // Decorator still works for lambdas
        Order functionalRushWithEmail = new EmailConfirmationOrder(functionalRush, emailService);

        functionalRegular.process("Carlos");           // No email
        functionalRushWithEmail.process("Diana");      // With email

        // === Functional advantage: add new order types dynamically ===
        Order flashSaleOrder = customer -> System.out.println("Processing FLASH SALE order for " + customer);
        flashSaleOrder.process("Eva");

        // === Add new decorators dynamically ===
        Order loggingDecorator = customer -> {
            System.out.println("[LOG] Starting order for " + customer);
            functionalRegular.process(customer);
            System.out.println("[LOG] Finished order for " + customer);
        };

        loggingDecorator.process("Frank");

        // Compose multiple behaviors: log + email
        Order fullDecoratedOrder = new EmailConfirmationOrder(loggingDecorator, emailService);
        fullDecoratedOrder.process("Grace");
    }
}
