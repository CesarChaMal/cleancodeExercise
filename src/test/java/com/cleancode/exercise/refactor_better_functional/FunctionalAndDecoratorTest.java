package com.cleancode.exercise.refactor_better_functional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionalAndDecoratorTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void processor_handlesConcreteOrders() {
        OrderProcessor processor = new OrderProcessor();
        Order order = new RegularOrder();

        processor.process(order, "Alice");

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("regular"), "Should process regular order");
        assertTrue(!output.toLowerCase().contains("confirmation"), "No confirmation unless decorated");
    }

    @Test
    void processor_handlesLambdaOrder() {
        OrderProcessor processor = new OrderProcessor();
        Order lambdaOrder = (customer) -> System.out.println("Processing custom order for " + customer);

        processor.process(lambdaOrder, "Bob");

        String output = out.toString();
        assertTrue(output.contains("Processing custom order for Bob"), "Should execute lambda-based order");
    }

    @Test
    void decorator_wrapsLambdaOrderAndSendsEmail() {
        EmailService emailService = new EmailService();
        Order lambdaOrder = (customer) -> System.out.println("Lambda order for " + customer);
        Order decorated = new EmailConfirmationOrder(lambdaOrder, emailService);

        decorated.process("Charlie");

        String output = out.toString();
        assertTrue(output.contains("Lambda order for Charlie"), "Should execute provided lambda");
        assertTrue(output.toLowerCase().contains("confirmation"), "Should send confirmation via decorator");
    }
}