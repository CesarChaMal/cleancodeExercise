package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

public final class OrderFactory {
    private OrderFactory() {}

    public static Order from(OrderType type) {
/*
        return switch (type) {
            case REGULAR -> Order.of(c -> System.out.println("Processing regular order for " + c));
            case RUSH    -> Order.of(c -> System.out.println("Processing rush order for " + c));
            case UNKNOWN -> Order.of(c -> System.out.println("Processing unknown order for " + c));
        };
*/
/*
        return switch (type) {
            case REGULAR -> new RegularOrder();
            case RUSH    -> new RushOrder();
            case UNKNOWN -> new UnknownOrder();
        };
*/
        return switch (type) {
            case REGULAR -> RegularOrder::new;
            case RUSH    -> RushOrder::new;
            case UNKNOWN -> UnknownOrder::new;
        };
    }
}