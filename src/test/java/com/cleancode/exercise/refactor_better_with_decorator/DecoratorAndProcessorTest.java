package com.cleancode.exercise.refactor_better_with_decorator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DecoratorAndProcessorTest {
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
    void decorator_addsEmailConfirmation() {
        EmailService emailService = new EmailService();
        Order base = new RegularOrder();
        Order decorated = new EmailConfirmationOrder(base, emailService);

        decorated.process("Alice");

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("regular"), "Base behavior should be executed");
        assertTrue(output.toLowerCase().contains("confirmation"), "Decorator should add email confirmation");
        assertTrue(output.contains("Alice"), "Should include customer name");
    }

    @Test
    void processor_worksWithPlainOrders() {
        OrderProcessor processor = new OrderProcessor();
        processor.process(new RushOrder(), "Bob");

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("rush"), "Processor should delegate to order");
        assertTrue(!output.toLowerCase().contains("confirmation"), "Processor alone should not send email");
    }

    @Test
    void processor_worksWithDecoratedOrders() {
        EmailService emailService = new EmailService();
        Order decorated = new EmailConfirmationOrder(new UnknownOrder(), emailService);
        OrderProcessor processor = new OrderProcessor();

        processor.process(decorated, "Charlie");

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("unknown"), "Underlying unknown order should run");
        assertTrue(output.toLowerCase().contains("confirmation"), "Decorator should send confirmation");
    }
}