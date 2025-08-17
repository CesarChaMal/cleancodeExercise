package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features;

import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class DecoratorStrategies {
    private DecoratorStrategies() {}

    public static Map<DecoratorType, UnaryOperator<Order>> basic(PrintStream out) {
        var map = getOperatorEnumMap();
        addIdentityFunction(map);
        addSingleRegistries(out, map);
        return Map.copyOf(map);
    }

    // Email-capable map using a sender function
    public static Map<DecoratorType, UnaryOperator<Order>> withEmail(PrintStream out, EmailSender emailSender) {
        var map = getOperatorEnumMap();
        addIdentityFunction(map);
        // Email-aware rich decorators: operate on Customer, only send if email is present
        addEmailRegistries(emailSender, map);
        // include basics for a single registry
        addSingleRegistries(out, map);
        return Map.copyOf(map);
    }
    private static void addIdentityFunction(EnumMap<DecoratorType, UnaryOperator<Order>> map) {
        map.put(DecoratorType.NONE, UnaryOperator.identity());
    }

    private static void addSingleRegistries(PrintStream out, EnumMap<DecoratorType, UnaryOperator<Order>> map) {
        map.put(DecoratorType.LOGGING, order -> Orders.logged(order, out, "LOG"));
        map.put(DecoratorType.TIMING, order -> Orders.timed(order, out));
        map.put(DecoratorType.RETRY_3, order -> Orders.retried(order, 3));
    }

    private static void addEmailRegistries(EmailSender emailSender, EnumMap<DecoratorType, UnaryOperator<Order>> map) {
        map.put(DecoratorType.EMAIL_SYNC, order -> order.andThen(name -> emailSender.send(name)));
        map.put(DecoratorType.EMAIL_ASYNC, order -> order.andThen(name -> {
            if (emailSender instanceof AsyncEmailSender a) a.sendAsync(name);
            else emailSender.send(name);
        }));
    }

    private static EnumMap<DecoratorType, UnaryOperator<Order>> getOperatorEnumMap() {
        return new EnumMap<>(DecoratorType.class);
    }
}