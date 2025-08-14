package com.cleancode.exercise.refactor_good;

public class OrderProcessor {
    enum OrderType {
        REGULAR, RUSH, UNKNOWN
    }

    public void processOrder(String customerName, OrderType orderType, boolean sendConfirmation) {
        handleOrder(customerName, orderType);

        if (sendConfirmation) {
            sendConfirmation(customerName);
        }
    }

    private void sendConfirmation(String customerName) {
        System.out.println("Sending confirmation email to" + customerName + "...");
        // simulate email
    }

    private void handleOrder(String customer, OrderType orderType) {
        switch (orderType) {
            case REGULAR -> System.out.println("Processing regular order for " + customer);
            case RUSH -> System.out.println("Processing rush order for " + customer);
            default -> System.out.println("Unknown order type for " + customer);
        }
    }
}
