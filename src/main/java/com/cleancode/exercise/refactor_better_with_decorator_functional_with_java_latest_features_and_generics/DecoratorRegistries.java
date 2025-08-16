package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;
import java.io.PrintStream;
import java.util.Map;
import java.util.function.UnaryOperator;

public record DecoratorRegistries<K extends Enum<K>, T>(
        Map<K, UnaryOperator<T>> basic,
        Map<K, UnaryOperator<T>> email
) {
    public static DecoratorRegistries<DecoratorType, Order> simple(PrintStream out, EmailSender sender) {
        return new DecoratorRegistries<>(
                DecoratorStrategies.basic(out),
                DecoratorStrategies.withEmail(out, sender)
        );
    }

    public static DecoratorRegistries<RichDecoratorType, RichOrder> rich(PrintStream out, EmailSender sender) {
        return new DecoratorRegistries<>(
                RichDecoratorStrategies.basic(out),
                RichDecoratorStrategies.withEmail(out, sender)
        );
    }
}