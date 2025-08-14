package com.cleancode.exercise.refactor_better;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderProcessorTest {
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
    void process_delegatesToOrder() {
        EmailService emailService = new EmailService();
        OrderProcessor processor = new OrderProcessor(emailService);
        Order order = new RegularOrder();

        processor.process(order, "Alice");

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("regular"), "Should process regular order");
        assertTrue(output.contains("Alice"), "Should include customer name");
        assertTrue(!output.toLowerCase().contains("confirmation"), "Plain process should not send email");
    }

    @Test
    void processWithEmail_sendsConfirmation() {
        EmailService emailService = new EmailService();
        OrderProcessor processor = new OrderProcessor(emailService);
        Order order = new RushOrder();

        processor.processWithEmail(order, "Bob");

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("rush"),
                "Should process rush order");
        assertTrue(output.toLowerCase().contains("confirmation"),
                "Should send email confirmation");
        assertTrue(output.contains("Bob"), "Should include customer name");
    }

    @Test
    void process_unknownOrderStillHandled() {
        EmailService emailService = new EmailService();
        OrderProcessor processor = new OrderProcessor(emailService);
        Order order = new UnknownOrder();

        processor.process(order, "Charlie");

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("unknown"), "Should handle unknown order variant");
        assertTrue(output.contains("Charlie"), "Should include customer name");
    }
}