# Clean Code Exercise — Progressive Refactors

This project demonstrates a progression of refactorings of a small “order processing” example. Each package shows a more maintainable and extensible approach than the previous one.

Versions order (from earliest to latest):
1) original
2) refactor_good
3) refactor_better
4) refactor_better_with_decorator
5) refactor_better_with_decorator_functional
6) refactor_better_with_decorator_functional_with_java_latest_features
7) refactor_better_with_decorator_functional_with_java_latest_features_and_generics

## What each version teaches

Here’s a clear, side‑by‑side comparison of all six iterations, highlighting what each one improved, and why the sixth (“decorator functional with latest Java features”) is the strongest so far.

1) Original (v1)
- Style: imperative, switch/if with type codes and a boolean flag to send email.
- Pros: simple for tiny scope.
- Cons:
    - Low cohesion: core logic and cross‑cutting concerns (email) are tangled.
    - Closed to extension: adding a new order type or behavior requires touching conditional logic.
    - Flag arguments obscure intent; hard to test edge cases cleanly.
    - Violates Single Responsibility and Open/Closed principles.

2) Good (v2)
- Style: improved imperative with clearer structure and naming (still centralized handling).
- Pros:
    - Better naming/structure; slightly easier to read and test.
- Cons:
    - Still driven by conditionals/flags; cross‑cutting concerns still mixed in.
    - Still not open to extension; still centralized branching.

3) Better (v3)
- Style: Strategy through an Order abstraction (interface) plus dependency injection for collaborators.
- Pros:
    - Eliminates type codes and flags in the call site.
    - Behavior varies by swapping Order implementations (Strategy). Cleaner unit tests.
    - DI makes collaborators mockable and reduces coupling.
- Cons:
    - Cross‑cutting concerns (email, timing, logging) still handled in the “processor” or in ad‑hoc ways.
    - Runtime composition is possible but not particularly ergonomic.

4) Better with Decorator (v4)
- Style: Strategy + Decorator. Email is moved into a decorator that wraps an Order.
- Pros:
    - Clean separation of concerns: email no longer pollutes core order processing.
    - You can mix and match “base” Orders and “email” decorator transparently.
    - Open/Closed compliance: add behaviors without modifying existing classes.
- Cons:
    - Class proliferation: each new behavior tends to be a new decorator class.
    - Composition is manual and can get verbose if you add logging, timing, retry, etc.

5) Decorator Functional (v5)
- Style: functional Strategy with a @FunctionalInterface and composition helpers; decorators as higher‑order functions.
- Pros:
    - First‑class composition: andThen, utility decorators like logged/timed/retried return a new Order without new classes.
    - Minimal class count; adding behaviors is just a small function.
    - Great testability; tiny pieces are trivial to verify.
- Cons:
    - Email behavior still ties to a concrete service unless abstracted.
    - Pipelines are powerful but need minimal helper registries/utilities for reuse.

6) Decorator Functional with latest Java features (v6)
- Style: v5 plus modern Java and richer domain abstractions.
- What’s new and why it matters:
    - Strategy creation via factory + enum (or lambdas): clean runtime selection of base behavior.
    - RichOrder + Customer record: enables domain‑aware pipelines (e.g., email depends on customer.email).
    - Adapters (Order ↔ RichOrder): reuse pipelines across string‑only and domain‑rich contexts without rewriting everything.
    - Cross‑cutting utilities: Orders.logged/timed/retried as pure functions; compose arbitrarily at runtime.
    - Email abstractions:
        - EmailService for sync + async (virtual threads) sending.
        - EmailSender (functional) for easy plugging/mocking in decorators/registries.
    - Optional decorator registry: DecoratorStrategies maps DecoratorType to functions, enabling declarative pipelines (LOGGING → TIMING → EMAIL_SYNC) without bespoke wiring.
- Pros:
    - Maximum composability with minimal code; no class explosion.
    - Strongest separation of concerns: core behavior vs. cross‑cutting vs. infrastructure.
    - Extensible: add new orders and decorators with one‑liners; zero churn in existing code.
    - Testable: each strategy and decorator is tiny and pure, easy to assert.
    - Modern concurrency: virtual threads for cheap async email.
    - Domain alignment: you can process simple names or rich Customers and bridge between them seamlessly.

7) Decorator Functional with latest Java features AND generics (v7)
- Style: v6 plus generic composition utilities that remove duplication between simple (Order) and rich (RichOrder) flows.
- What’s new and why it matters:
    - DecoratorRegistries<K, T>: a generic record holding two typed maps (basic and email) where K is an enum (DecoratorType or RichDecoratorType) and T is the pipeline type (Order or RichOrder).
        - Factory methods: simple(...) and rich(...) build typed registries from the existing strategy providers.
    - Pipelines: a small generic utility to assemble pipelines declaratively:
        - threeStage(base, registries, LOGGING, RETRY_3, EMAIL_ASYNC)
        - build(base, stageMap, LOGGING, TIMING, …)
    - Benefits:
        - DRY: eliminates duplicated “basic + email + apply in order” code across tests and the Main class.
        - Type‑safe: compile‑time safety for both simple and rich flows.
        - Readable: intent first; wiring is standardized and reusable.
- Pros:
    - Everything from v6, plus simpler, reusable wiring via generics.
    - Ideal for larger teams where consistency of pipeline assembly matters.
- Cons:
    - Slightly more conceptual surface (a generic holder + pipeline helper), but public APIs remain approachable.

### Why v6/v7 are the best (in theory and practice)
- They combine the clarity and extensibility of Strategy/Decorator (v3/v4) with the ergonomics and power of functions (v5).
- They support both simple and rich domain pipelines with zero duplication (thanks to generics in v7).
- Future‑proof: adding observability, resilience, or async composition is a matter of composing functions, not creating and wiring more classes.
- Approachable: simple APIs (Order, Orders, withEmail, factory methods) + optional registries and generic helpers keep usage consistent and readable.

### When you might still choose earlier versions
- v3 (“better”) for teams more comfortable with OO and DI while keeping code size small, when cross‑cutting needs are limited.
- v4 (“better with decorator”) if you want explicit OO decorators for codebase conventions or tooling reasons.
- v5 when you want the functional style but don’t need richer domain objects or async/registries yet.

### Recommended direction
- Keep v6/v7 as your primary approach. They offer the best trade‑offs:
    - Strategy selection via factory or lambdas.
    - Declarative composition of behaviors at runtime.
    - Domain‑aware pipelines via RichOrder when needed.
    - Modern async via virtual threads.
    - DRY, type‑safe pipeline wiring via DecoratorRegistries and Pipelines (v7).
- Maintain small helper registries (like DecoratorStrategies/RichDecoratorStrategies) for consistency and discoverability, and use DecoratorRegistries + Pipelines where composition repeats.

In short: v7 is the strongest and most maintainable version so far. It adheres to SOLID, enables open/closed extensibility, favors composition over inheritance, leverages modern Java, and removes boilerplate with generics.

## Project structure

- src/main/java/com/cleancode/exercise
    - original
    - refactor_good
    - refactor_better
    - refactor_better_with_decorator
    - refactor_better_with_decorator_functional
    - refactor_better_with_decorator_functional_with_java_latest_features
    - refactor_better_with_decorator_functional_with_java_latest_features_and_generics

Some versions include a Main class to run examples directly (notably in `refactor_better`, `refactor_better_with_decorator`, `refactor_better_with_decorator_functional`, `refactor_better_with_decorator_functional_with_java_latest_features` and `refactor_better_with_decorator_functional_with_java_latest_features_and_generics`).

## Requirements

- Java 24+ (project config targets 24)
- Maven 3.8+

## Build

```bash
mvn -q -DskipTests package
```
This produces compiled classes in `target/classes`.

## How to run

You can run any version that has a `Main` class using the Java command below. If you use an IDE, you can run the corresponding Main class directly from the editor.

- Refactor (better):
```bash
java -cp target/classes com.cleancode.exercise.refactor_better.Main
```
- Refactor (decorator):
```bash
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator.Main
```
- Refactor (functional):
```bash
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator_functional.Main
```
- Refactor (functional with java latest features):
```bash
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features.Main
```
- Refactor (functional with java latest features and generics):
```bash
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics.Main
```
If you prefer Maven Exec Plugin, you can add it to the build and run with `-Dexec.mainClass=...`. As-is, the plugin is not configured in this project.

## Design highlights

- Separation of concerns
    - Order types encapsulate order-specific behavior.
    - EmailService (and functional EmailSender) encapsulate email responsibilities.
    - Optional processors/registries orchestrate higher-level workflows.

- Composition over conditionals
    - Adding new order types: implement the Order interface (or provide a lambda in functional versions).
    - Adding cross-cutting behavior: wrap an existing Order with decorators (OO or functional).

- Functional flexibility (v5–v6)
    - Order is a functional interface, allowing concise lambdas and easy composition with decorators or other higher-order functions.
    - v6 adds RichOrder + Customer, adapters (Order ↔ RichOrder), and virtual-thread async email.

## Clean code and SOLID practices demonstrated

- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Liskov Substitution Principle (LSP)
- Interface Segregation Principle (ISP)
- Dependency Inversion Principle (DIP)
- Composition over inheritance
- Elimination of magic numbers/flags
- Testability and replaceability
- Side-effect isolation
- Progressive disclosure of complexity

## Extending the system

- New order type (v3+)
    - Create a new class implementing Order, or provide a lambda (functional versions).
    - No changes needed to existing processor logic.

- New cross-cutting behavior
    - OO: create a decorator class that implements Order and wraps another Order.
    - Functional: add a higher-order function in Orders (e.g., logged/timed/retried) or wire via DecoratorStrategies.

## Notes

- Some earlier versions do not include a `Main` class; they are intended to illustrate refactoring direction rather than be executed directly.
