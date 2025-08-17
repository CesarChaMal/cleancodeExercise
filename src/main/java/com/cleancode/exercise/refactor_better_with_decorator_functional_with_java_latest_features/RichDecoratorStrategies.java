package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features;

import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class RichDecoratorStrategies {
    private RichDecoratorStrategies() {}

    // Adapt a simple Order decorator to a RichOrder decorator using the adapters
    private static UnaryOperator<RichOrder> wrapSimpleDecorator(UnaryOperator<Order> simpleDecorator) {
        return rich -> {
            var simple = OrderAdapters.toSimple(rich);
            var decoratedSimple = simpleDecorator.apply(simple);
            return OrderAdapters.toRich(decoratedSimple);
        };
    }

    public static Map<RichDecoratorType, UnaryOperator<RichOrder>> basic(PrintStream out) {
        var map = getOperatorEnumMap();
        addIdentityFunction(map);
        addSingleRegistries(out, map);
        return Map.copyOf(map);
    }

    public static Map<RichDecoratorType, UnaryOperator<RichOrder>> withEmail(PrintStream out, EmailSender emailSender) {
        var map = getOperatorEnumMap();
        addIdentityFunction(map);
        // Email-aware rich decorators: operate on Customer, only send if email is present
        addEmailRegistries(emailSender, map);
        // Include basics so callers can use a single registry if they want
        addSingleRegistries(out, map);
        return Map.copyOf(map);
    }

    private static void addIdentityFunction(EnumMap<RichDecoratorType, UnaryOperator<RichOrder>> map) {
        map.put(RichDecoratorType.NONE, UnaryOperator.identity());
    }

    private static void addSingleRegistries(PrintStream out, EnumMap<RichDecoratorType, UnaryOperator<RichOrder>> map) {
        map.put(RichDecoratorType.LOGGING, wrapSimpleDecorator(order -> Orders.logged(order, out, "RICH")));
        map.put(RichDecoratorType.TIMING,  wrapSimpleDecorator(order -> Orders.timed(order, out)));
        map.put(RichDecoratorType.RETRY_3, wrapSimpleDecorator(order -> Orders.retried(order, 3)));
    }

    private static void addEmailRegistries(EmailSender emailSender, EnumMap<RichDecoratorType, UnaryOperator<RichOrder>> map) {
        map.put(RichDecoratorType.EMAIL_SYNC, rich -> RichDecorators.withEmail(rich, emailSender));
        map.put(RichDecoratorType.EMAIL_ASYNC, rich -> customer -> {
            rich.process(customer);
            if (customer.email() != null && !customer.email().isBlank()) {
                if (emailSender instanceof AsyncEmailSender a) a.sendAsync(customer.name());
                else emailSender.send(customer.name());
            }
        });
    }

    private static EnumMap<RichDecoratorType, UnaryOperator<RichOrder>> getOperatorEnumMap() {
        return new EnumMap<>(RichDecoratorType.class);
    }
}