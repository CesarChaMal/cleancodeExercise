// Java
package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features;

public final class OrderAdapters {
    private OrderAdapters() {}

    public static RichOrder toRich(Order simple) {
        return customer -> simple.process(customer.name());
    }

    public static Order toSimple(RichOrder rich) {
        return name -> rich.process(new Customer(name, null));
    }
}