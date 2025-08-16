package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class DecoratorStrategies {
    private DecoratorStrategies() {}

    public static Map<DecoratorType, UnaryOperator<Order>> basic(PrintStream out) {
        var map = new EnumMap<DecoratorType, UnaryOperator<Order>>(DecoratorType.class);
        map.put(DecoratorType.LOGGING, order -> Orders.logged(order, out, "LOG"));
        map.put(DecoratorType.TIMING, order -> Orders.timed(order, out));
        map.put(DecoratorType.RETRY_3, order -> Orders.retried(order, 3));
        return Map.copyOf(map);
    }

    // Email-capable map using a sender function
    public static Map<DecoratorType, UnaryOperator<Order>> withEmail(PrintStream out, EmailSender emailSender) {
        var map = new EnumMap<DecoratorType, UnaryOperator<Order>>(DecoratorType.class);
        map.put(DecoratorType.EMAIL_SYNC, order -> order.andThen(name -> emailSender.send(name)));
        map.put(DecoratorType.EMAIL_ASYNC, order -> order.andThen(name -> {
            if (emailSender instanceof AsyncEmailSender a) a.sendAsync(name);
            else emailSender.send(name);
        }));
        // include basics for a single registry
        map.put(DecoratorType.LOGGING, order -> Orders.logged(order, out, "LOG"));
        map.put(DecoratorType.TIMING, order -> Orders.timed(order, out));
        map.put(DecoratorType.RETRY_3, order -> Orders.retried(order, 3));
        return Map.copyOf(map);
    }
}