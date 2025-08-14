package com.cleancode.exercise.refactor_good;

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

        // Assuming same parameterization (type codes and email flag) in this refactor step
        processor.processOrder("Alice", OrderProcessor.OrderType.REGULAR, false);

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("regular"), "Should include regular order processing");
        assertTrue(output.contains("Alice"), "Should include customer name");
        assertTrue(!output.toLowerCase().contains("sending confirmation"), "Should not send email when flag is false");
    }

    @Test
    void processesRushOrderWithEmail() {
        OrderProcessor processor = new OrderProcessor();

        processor.processOrder("Bob", OrderProcessor.OrderType.RUSH, true);

        String output = out.toString();
        assertTrue(output.toLowerCase().contains("rush"), "Should include rush order processing");
        assertTrue(output.contains("Bob"), "Should include customer name");
        assertTrue(output.toLowerCase().contains("sending confirmation"), "Should include email confirmation");
    }
}