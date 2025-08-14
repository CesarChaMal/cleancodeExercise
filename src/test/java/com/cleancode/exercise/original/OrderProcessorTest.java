package com.cleancode.exercise.original;

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
    void processesRegularOrderWithoutEmail() {
        OrderProcessor processor = new OrderProcessor();

        processor.processOrder("Alice", 1, false);

        String output = out.toString();
        assertTrue(output.contains("Processing regular order for Alice"),
                "Should process regular order message");
        // should not contain email send message
        assertTrue(!output.toLowerCase().contains("sending confirmation"),
                "Should not send email when flag is false");
    }

    @Test
    void processesRushOrderWithEmail() {
        OrderProcessor processor = new OrderProcessor();

        processor.processOrder("Bob", 2, true);

        String output = out.toString();
        assertTrue(output.contains("Processing rush order for Bob"), "Should process rush order message");
        assertTrue(output.toLowerCase().contains("sending confirmation"), "Should send email when flag is true");
    }

    @Test
    void processesUnknownOrder() {
        OrderProcessor processor = new OrderProcessor();

        processor.processOrder("Charlie", 99, false);

        String output = out.toString();
        assertTrue(output.contains("Unknown order type for Charlie"), "Should handle unknown order type");
    }
}