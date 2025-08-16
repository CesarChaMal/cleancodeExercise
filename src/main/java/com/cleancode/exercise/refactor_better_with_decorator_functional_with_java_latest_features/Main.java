package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features;

import java.util.function.UnaryOperator;

public class Main {
    public static void main(String[] args) {
        var emailService = new EmailService();
        EmailSender syncSender = emailService::sendConfirmation;
        var asyncSender = new AsyncEmailSender();

        // === Base orders selected via factory (strategies) ===
        var regular = OrderFactory.from(OrderType.REGULAR);
        var rush    = OrderFactory.from(OrderType.RUSH);

        // === Build decorator registries ===
        var basicDecorators = DecoratorStrategies.basic(System.out);
        var emailDecoratorsSync  = DecoratorStrategies.withEmail(System.out, syncSender);
        var emailDecoratorsAsync = DecoratorStrategies.withEmail(System.out, asyncSender);

        // LOG -> NONE -> EMAIL_SYNC (skips middle stage)
        var pipeline1 = apply(regular, basicDecorators.get(DecoratorType.LOGGING));
        pipeline1 = apply(pipeline1, basicDecorators.get(DecoratorType.NONE));
        pipeline1 = apply(pipeline1, emailDecoratorsSync.get(DecoratorType.EMAIL_SYNC));
        pipeline1.process("Alice");

        // NONE -> TIMING -> EMAIL_ASYNC (skips logging)
        var pipeline2 = apply(rush, basicDecorators.get(DecoratorType.NONE));
        pipeline2 = apply(pipeline2, basicDecorators.get(DecoratorType.TIMING));
        pipeline2 = apply(pipeline2, emailDecoratorsAsync.get(DecoratorType.EMAIL_ASYNC));
        pipeline2.process("Bob");

        // LOG -> TIMING -> NONE (no email)
        var custom = Order.of(c -> System.out.println("Processing FLASH SALE order for " + c));
        var pipeline3 = apply(custom, basicDecorators.get(DecoratorType.LOGGING));
        pipeline3 = apply(pipeline3, basicDecorators.get(DecoratorType.TIMING));
        pipeline3 = apply(pipeline3, emailDecoratorsSync.get(DecoratorType.NONE));
        pipeline3.process("Carlos");

        // === Ad-hoc functional order ===
        var flashSale = Order.of(c -> System.out.println("Processing FLASH SALE order for " + c));
        var flashWithLogAndEmail = apply(flashSale, basicDecorators.get(DecoratorType.LOGGING))
                .withEmail(emailService);
        flashWithLogAndEmail.process("Carlos");

        // === RichOrder usage (operate on Customer) ===
        // Adapt a simple Order to RichOrder (works with Customer)
        var richFromSimple = OrderAdapters.toRich(regular);
        // Decorate with email that uses customer data (RichDecorators)
        var richWithEmail = RichDecorators.withEmail(richFromSimple, syncSender);
        richWithEmail.process(new Customer("Diana", "diana@example.com"));

        // Adapt RichOrder back to simple Order if needed
        var simpleFromRich = OrderAdapters.toSimple(richWithEmail);
        simpleFromRich.process("Eve"); // email is null here, so no email will be sent

        // === RichDecoratorStrategies usage (Customer-based pipelines) ===
        var richBasic = RichDecoratorStrategies.basic(System.out);
        var richEmailSync = RichDecoratorStrategies.withEmail(System.out, syncSender);
        var richEmailAsync = RichDecoratorStrategies.withEmail(System.out, asyncSender);

        // Compose rich pipeline: LOG -> TIME -> EMAIL_SYNC on richFromSimple
        var richPipeline = richBasic.get(RichDecoratorType.LOGGING).apply(richFromSimple);
        richPipeline = richBasic.get(RichDecoratorType.TIMING).apply(richPipeline);
        richPipeline = richEmailSync.get(RichDecoratorType.EMAIL_SYNC).apply(richPipeline);
        richPipeline.process(new Customer("Iris", "iris@example.com"));

        // Compose rich pipeline: LOG -> EMAIL_ASYNC on richFromSimple
        var richAsyncPipeline = richBasic.get(RichDecoratorType.LOGGING).apply(richFromSimple);
        richAsyncPipeline = richEmailAsync.get(RichDecoratorType.EMAIL_ASYNC).apply(richAsyncPipeline);
        richAsyncPipeline.process(new Customer("Jack", "jack@example.com"));

        // Close async sender (virtual-thread executor)
        asyncSender.close();
    }

    private static Order apply(Order base, UnaryOperator<Order> decorator) {
        return decorator.apply(base);
    }
}