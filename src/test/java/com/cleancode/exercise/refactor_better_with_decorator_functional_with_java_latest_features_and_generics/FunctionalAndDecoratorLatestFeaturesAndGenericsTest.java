package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.UnaryOperator;

import static com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics.DecoratorRegistries.rich;
import static com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics.DecoratorRegistries.simple;
import static com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics.Pipelines.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionalAndDecoratorLatestFeaturesAndGenericsTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void factory_handlesConcreteOrders() {
        // Obtain a base Order via the strategy/factory
        Order order = OrderFactory.from(OrderType.REGULAR);

        // Ensure visible output irrespective of the base implementation
        order = Orders.logged(order, System.out, "TEST");

        order.process("Alice");

        String output = out.toString().toLowerCase();
        assertTrue(output.contains("[test]"), "Should log via decorator");
        assertTrue(output.contains("alice"), "Should include customer name");
        assertTrue(!output.contains("confirmation"), "No confirmation unless decorated");
    }

    @Test
    void lambda_order_executes() {
        // Lambda-based Order produces its own output
        Order lambdaOrder = Order.of(customer -> System.out.println("Processing custom order for " + customer));

        lambdaOrder.process("Bob");

        String output = out.toString();
        assertTrue(output.contains("Processing custom order for Bob"), "Should execute lambda-based order");
    }

    @Test
    void decorator_via_defaultMethod_sendsEmail() {
        var emailService = new EmailService();
        Order lambdaOrder = Order.of(customer -> System.out.println("Lambda order for " + customer));

        // Decorate using the default withEmail(...) method
        Order decorated = lambdaOrder.withEmail(emailService);

        decorated.process("Charlie");

        String output = out.toString();
        assertTrue(output.contains("Lambda order for Charlie"), "Should execute provided lambda");
        assertTrue(output.toLowerCase().contains("confirmation"), "Should send confirmation via decorator");
        assertTrue(output.contains("Charlie"), "Should include customer name");
    }

    @Test
    void adapter_convertsBetweenSimpleAndRichOrder() {
        // Use a visible simple Order (lambda) so delegation is observable
        Order simpleVisible = Order.of(name -> System.out.println("Visible simple order for " + name));

        var rich = OrderAdapters.toRich(simpleVisible);
        rich.process(new Customer("Dana", "dana@example.com"));

        var output1 = out.toString();
        assertTrue(output1.contains("Visible simple order for Dana"), "RichOrder should delegate to simple order");

        out.reset();

        var simple = OrderAdapters.toSimple(rich);
        simple.process("Eli");

        var output2 = out.toString();
        assertTrue(output2.contains("Visible simple order for Eli"), "Simple adapter should pass name into RichOrder");
    }

    @Test
    void strategy_selectsImplementationAtRuntime() {
        var selectedType = OrderType.RUSH; // could be dynamic
        var selectedOrder = Orders.logged(OrderFactory.from(selectedType), System.out, "RUNTIME");

        selectedOrder.process("Fiona");

        var output = out.toString().toLowerCase();
        assertTrue(output.contains("[runtime]"), "Should log via runtime-chosen strategy");
        assertTrue(output.contains("fiona"), "Should include customer name");
    }

    @Test
    void async_email_can_be_composed_functionally() throws Exception {
        var emailService = new EmailService();
        var rush = Orders.logged(OrderFactory.from(OrderType.RUSH), System.out, "RUSH");

        var rushWithAsyncEmail = Order.of(c -> {
            rush.process(c);
            emailService.sendConfirmationAsync(c);
        });

        rushWithAsyncEmail.process("George");

        // Small wait to allow async print to appear
        Thread.sleep(50);

        var output = out.toString().toLowerCase();
        assertTrue(output.contains("[rush]"), "Should log rush processing");
        assertTrue(output.contains("george"), "Should include customer name");
        assertTrue(output.contains("sending confirmation"), "Should send confirmation email asynchronously");
    }

    @Test
    void decorator_registry_applies_logging_timing_and_sync_email() {
        var emailService = new EmailService();
        EmailSender syncSender = emailService::sendConfirmation;

        var base = OrderFactory.from(OrderType.REGULAR);

        var registries = simple(System.out, syncSender);

        var pipeline = loggingTimingEmail(base, registries, DecoratorType.LOGGING,DecoratorType.TIMING, DecoratorType.EMAIL_ASYNC);

        pipeline.process("Helen");

        var outStr = out.toString().toLowerCase();
        assertTrue(outStr.contains("[log]"), "Should log before/after");
        assertTrue(outStr.contains("processed"), "Should include timing output");
        assertTrue(outStr.contains("helen"), "Should include customer name");
        assertTrue(outStr.contains("sending confirmation"), "Should send confirmation via sync decorator");
    }
    @Test
    void decorator_registry_applies_logging_retry_and_async_email() throws Exception {
        var asyncSender = new AsyncEmailSender();
        try {
            var base = OrderFactory.from(OrderType.RUSH);

//            var registries = buildOrderRegistries(System.out, asyncSender);
            var registries = simple(System.out, asyncSender);

            // use generic pipeline helper with simple (Order) keys
/*
            var pipeline = getPipeline(
                    base,
                    registries,
                    DecoratorType.LOGGING,
                    DecoratorType.RETRY_3,
                    DecoratorType.EMAIL_ASYNC
            );
*/
            var pipeline = loggingRetryEmail(base, registries, DecoratorType.LOGGING, DecoratorType.RETRY_3, DecoratorType.EMAIL_ASYNC);

            pipeline.process("Ian");

            // Allow async output to flush
            Thread.sleep(50);

            var outStr = out.toString().toLowerCase();
            assertTrue(outStr.contains("[log]"), "Should log events");
            assertTrue(outStr.contains("ian"), "Should include customer name");
            assertTrue(outStr.contains("sending confirmation"), "Should send confirmation via async decorator");
        } finally {
            asyncSender.close();
        }
    }

    @Test
    void decorator_registry_supports_NONE_to_skip_stages() {
        var emailService = new EmailService();
        EmailSender syncSender = emailService::sendConfirmation;

        var base = OrderFactory.from(OrderType.REGULAR);
        var registries = simple(System.out, syncSender);

        // LOG -> NONE -> EMAIL_SYNC
        var p1 = threeStage(base, registries, DecoratorType.LOGGING, DecoratorType.NONE, DecoratorType.EMAIL_SYNC);
        p1.process("Nora");

        var out1 = out.toString().toLowerCase();
        assertTrue(out1.contains("[log]"), "Should log");
        assertTrue(out1.contains("nora"), "Should include name");
        assertTrue(out1.contains("sending confirmation"), "Email should be sent");

        out.reset();

        // LOG -> TIMING -> NONE
        var p2 = threeStage(base, registries, DecoratorType.LOGGING, DecoratorType.TIMING, DecoratorType.NONE);
        p2.process("Owen");

        var out2 = out.toString().toLowerCase();
        assertTrue(out2.contains("[log]"), "Should log");
        assertTrue(out2.contains("processed"), "Should include timing");
        assertTrue(!out2.contains("sending confirmation"), "No email when NONE is used");
    }

    @Test
    void rich_decorator_registry_applies_logging_timing_and_sync_email() {
        var emailService = new EmailService();
        EmailSender syncSender = emailService::sendConfirmation;

        var base = OrderFactory.from(OrderType.REGULAR);
        RichOrder richBase = OrderAdapters.toRich(base);

        var registries = rich(System.out, syncSender);

        var pipeline = loggingTimingEmail(richBase, registries, RichDecoratorType.LOGGING,RichDecoratorType.TIMING, RichDecoratorType.EMAIL_ASYNC);

        pipeline.process(new Customer("Helen", "helen@example.com"));

        var outStr = out.toString().toLowerCase();
        assertTrue(outStr.contains("[rich]"), "Should log before/after");
        assertTrue(outStr.contains("processed"), "Should include timing output");
        assertTrue(outStr.contains("helen"), "Should include customer name");
        assertTrue(outStr.contains("sending confirmation"), "Should send confirmation via sync decorator");
    }

    @Test
    void rich_decorator_registry_applies_logging_retry_and_async_email() throws Exception {
        var asyncSender = new AsyncEmailSender();
        try {
            // Start from a simple Order, then adapt to RichOrder for customer-aware pipeline
            var base = OrderFactory.from(OrderType.RUSH);
            RichOrder richBase = OrderAdapters.toRich(base);

//            var registries = buildRichRegistries(System.out, asyncSender);
            var registries = rich(System.out, asyncSender);

            // use the same generic helper with rich keys
/*
            var pipeline = getPipeline(
                    richBase,
                    registries,
                    RichDecoratorType.LOGGING,
                    RichDecoratorType.RETRY_3,
                    RichDecoratorType.EMAIL_ASYNC
            );
*/
            var pipeline = loggingRetryEmail(richBase, registries, RichDecoratorType.LOGGING, RichDecoratorType.RETRY_3, RichDecoratorType.EMAIL_ASYNC);

            // Rich pipeline expects a Customer
            pipeline.process(new Customer("Ian", "ian@example.com"));

            // Allow async output to flush
            Thread.sleep(50);

            var outStr = out.toString().toLowerCase();
            assertTrue(outStr.contains("[rich]"), "Should log events");
            assertTrue(outStr.contains("ian"), "Should include customer name");
            assertTrue(outStr.contains("sending confirmation"), "Should send confirmation via async decorator");
        } finally {
            asyncSender.close();
        }
    }

    @Test
    void rich_decorator_registry_supports_NONE_to_skip_stages() {
        var emailService = new EmailService();
        EmailSender syncSender = emailService::sendConfirmation;

        var base = OrderFactory.from(OrderType.REGULAR);
        var richBase = OrderAdapters.toRich(base);
        var registries = rich(System.out, syncSender);

        // LOG -> NONE -> EMAIL_SYNC
        var p1 = threeStage(richBase, registries, RichDecoratorType.LOGGING, RichDecoratorType.NONE, RichDecoratorType.EMAIL_SYNC);
        p1.process(new Customer("Nora", "nora@example.com"));

        var out1 = out.toString().toLowerCase();
        assertTrue(out1.contains("[rich]"), "Should log in rich pipeline");
        assertTrue(out1.contains("nora"), "Should include customer name");
        assertTrue(out1.contains("sending confirmation"), "Email should be sent");

        out.reset();

        // LOG -> TIMING -> NONE
        var p2 = threeStage(richBase, registries, RichDecoratorType.LOGGING, RichDecoratorType.TIMING, RichDecoratorType.NONE);
        p2.process(new Customer("Owen", "owen@example.com"));

        var out2 = out.toString().toLowerCase();
        assertTrue(out2.contains("[rich]"), "Should log");
        assertTrue(out2.contains("processed"), "Should include timing");
        assertTrue(!out2.contains("sending confirmation"), "No email when NONE is used");
    }

/*
    // Generic registries holder to avoid duplication across simple/rich variants
    private record Registries<K, T>(
            Map<K, UnaryOperator<T>> basic,
            Map<K, UnaryOperator<T>> email
    ) {}

    // Generic pipeline builder that works for both simple and rich flows
    private static <K, T> T getPipeline(T base, Registries<K, T> registries, K loggingKey, K retryKey, K emailAsyncKey) {
        var pipeline = apply(base, registries.basic().get(loggingKey));
        pipeline = apply(pipeline, registries.basic().get(retryKey));
        pipeline = apply(pipeline, registries.email().get(emailAsyncKey));
        return pipeline;
    }

    private static Registries<DecoratorType, Order> buildOrderRegistries(PrintStream out, EmailSender sender) {
        var basic = DecoratorStrategies.basic(out);
        var email = DecoratorStrategies.withEmail(out, sender);
        return new Registries<>(basic, email);
    }

    private static Registries<RichDecoratorType, RichOrder> buildRichRegistries(PrintStream out, EmailSender sender) {
        var basic = RichDecoratorStrategies.basic(out);
        var email = RichDecoratorStrategies.withEmail(out, sender);
        return new Registries<>(basic, email);
    }
*/

    private static <T> T apply(T base, UnaryOperator<T> decorator) {
        return decorator.apply(base);
    }
}