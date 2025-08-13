package com.cleancode.exercise.original;

public class OrderProcessor {
    public void processOrder(String customer, int t, boolean v) {
        if (t == 1) {
            System.out.println("Processing regular order for " + customer);
        } else if (t == 2) {
            System.out.println("Processing rush order for " + customer);
        } else {
            System.out.println("Unknown order type for " + customer);
        }

        if (v) {
            System.out.println("Sending confirmation email...");
            // simulate email
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
