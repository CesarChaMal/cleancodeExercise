package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

import java.util.function.UnaryOperator;

import static com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics.Pipelines.threeStage;

public class Main {
    public static void main(String[] args) {
        var emailService = new EmailService();
        EmailSender syncSender = emailService::sendConfirmation;
        var asyncSender = new AsyncEmailSender();

        // === Base orders selected via factory (strategies) ===
        var regular = OrderFactory.from(OrderType.REGULAR);
        var rush    = OrderFactory.from(OrderType.RUSH);

        // === Build generic decorator registries (simple and rich) ===
        var simpleSync  = DecoratorRegistries.simple(System.out, syncSender);
        var simpleAsync = DecoratorRegistries.simple(System.out, asyncSender);

        // === Compose simple pipelines with generics (matching your non-generic examples) ===

        // 1) LOG -> NONE -> EMAIL_SYNC on regular (skips middle stage)
        var pipeline1 = Pipelines.threeStage(regular, simpleSync, DecoratorType.LOGGING, DecoratorType.NONE, DecoratorType.EMAIL_SYNC);
        pipeline1.process("Alice");

        // 2) NONE -> TIMING -> EMAIL_ASYNC on rush (skips logging)
        var pipeline2 = Pipelines.threeStage(rush, simpleAsync, DecoratorType.NONE, DecoratorType.TIMING, DecoratorType.EMAIL_ASYNC);
        pipeline2.process("Bob");

        // 3) LOG -> TIMING -> NONE on flashSale (no email)
        var flashSale = Order.of(c -> System.out.println("Processing FLASH SALE order for " + c));
        var pipeline3 = Pipelines.loggingTimingEmail(flashSale, simpleSync, DecoratorType.LOGGING, DecoratorType.TIMING, DecoratorType.NONE);
        pipeline3.process("Carlos");

        // === RichOrder usage (operate on Customer) ===
        var richFromSimple = OrderAdapters.toRich(regular);

        // Build rich registries (customer-aware decorators)
        var richSync  = DecoratorRegistries.rich(System.out, syncSender);
        var richAsync = DecoratorRegistries.rich(System.out, asyncSender);

        // LOG -> TIME -> EMAIL_SYNC on richFromSimple
        var richPipeline = Pipelines.loggingTimingEmail(richFromSimple, richSync, RichDecoratorType.LOGGING, RichDecoratorType.TIMING, RichDecoratorType.EMAIL_SYNC);
        richPipeline.process(new Customer("Iris", "iris@example.com"));

        // LOG -> RETRY -> EMAIL_ASYNC on richFromSimple
        var richAsyncPipeline = Pipelines.loggingRetryEmail(richFromSimple, richAsync, RichDecoratorType.LOGGING, RichDecoratorType.RETRY_3, RichDecoratorType.EMAIL_ASYNC);
        richAsyncPipeline.process(new Customer("Jack", "jack@example.com"));

        // Close async sender (virtual-thread executor)
        asyncSender.close();
    }

    private static Order apply(Order base, UnaryOperator<Order> decorator) {
        return decorator.apply(base);
    }
}