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

        // === Compose simple pipelines with generics ===
        // LOG -> TIME -> EMAIL_SYNC on regular
        var regularPipeline = threeStage(
                regular, simpleSync,
                DecoratorType.LOGGING,
                DecoratorType.TIMING,
                DecoratorType.EMAIL_SYNC
        );
        regularPipeline.process("Alice");

        // LOG -> RETRY -> EMAIL_ASYNC on rush
        var rushPipeline = threeStage(
                rush, simpleAsync,
                DecoratorType.LOGGING,
                DecoratorType.RETRY_3,
                DecoratorType.EMAIL_ASYNC
        );
        rushPipeline.process("Bob");

        // === Ad-hoc functional order ===
        var flashSale = Order.of(c -> System.out.println("Processing FLASH SALE order for " + c));
        var flashWithLogAndEmail = threeStage(
                flashSale, simpleSync,
                DecoratorType.LOGGING,
                DecoratorType.TIMING,       // can be TIMING or another stage
                DecoratorType.EMAIL_SYNC
        );
        flashWithLogAndEmail.process("Carlos");

        // === RichOrder usage (operate on Customer) ===
        var richFromSimple = OrderAdapters.toRich(regular);

        // Build rich registries (customer-aware decorators)
        var richSync  = DecoratorRegistries.rich(System.out, syncSender);
        var richAsync = DecoratorRegistries.rich(System.out, asyncSender);

        // LOG -> TIME -> EMAIL_SYNC on richFromSimple
        var richPipeline = threeStage(
                richFromSimple, richSync,
                RichDecoratorType.LOGGING,
                RichDecoratorType.TIMING,
                RichDecoratorType.EMAIL_SYNC
        );
        richPipeline.process(new Customer("Iris", "iris@example.com"));

        // LOG -> RETRY -> EMAIL_ASYNC on richFromSimple
        var richAsyncPipeline = threeStage(
                richFromSimple, richAsync,
                RichDecoratorType.LOGGING,
                RichDecoratorType.RETRY_3,
                RichDecoratorType.EMAIL_ASYNC
        );
        richAsyncPipeline.process(new Customer("Jack", "jack@example.com"));

        // Close async sender (virtual-thread executor)
        asyncSender.close();
    }

    private static Order apply(Order base, UnaryOperator<Order> decorator) {
        return decorator.apply(base);
    }
}