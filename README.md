# Clean Code Exercise — Progressive Refactors

This project demonstrates a progression of refactorings of a small “order processing” example. Each package shows a more maintainable and extensible approach than the previous one.

Versions order (from earliest to latest):
1) original
2) refactor_good
3) refactor_better
4) refactor_better_with_decorator
5) refactor_better_functional

## What each version teaches

- original
    - Single method with conditionals, magic numbers/flags and mixed responsibilities.
    - Tight coupling and low readability.
    - Hard to add new order types or behaviors without changing existing code.

- refactor_good
    - Initial cleanup of naming and parameters.
    - Better readability but still limited extensibility.

- refactor_better
    - Introduces an Order interface and concrete implementations (e.g., Regular, Rush).
    - Extracts email behavior to a dedicated EmailService.
    - OrderProcessor delegates to Order and composes EmailService (dependency injection).
    - Open/Closed improvement: adding a new order type no longer changes the processor.

- refactor_better_with_decorator
    - Uses the Decorator pattern (e.g., EmailConfirmationOrder) to compose cross-cutting behavior (sending emails) around any Order without modifying core logic.
    - Encourages composition over inheritance and avoids conditionals for optional features.

- refactor_better_functional
    - Makes Order a functional interface to support lambdas and dynamic behavior creation.
    - Keeps decorator composition so the same cross-cutting features can wrap either classes or lambdas.
    - Maximizes flexibility and testability with minimal boilerplate.

## Project structure

- src/main/java/com/cleancode/exercise
    - original
    - refactor_good
    - refactor_better
    - refactor_better_with_decorator
    - refactor_better_functional

Some versions include a Main class to run examples directly (notably in `refactor_better`, `refactor_better_with_decorator`, and `refactor_better_functional`).

## Requirements

- Java 14+ (project config targets 14)
- Maven 3.8+

You're right—the “How to run” section wasn’t displaying correctly due to broken code fences and a missing command. Here’s a clean, fixed version of the Build and How to run sections.

You're right—those code fences were still off. Here’s a clean replacement for the Build and How to run sections with correctly formatted fences and spacing.

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
java -cp target/classes com.cleancode.exercise.refactor_better_functional.Main
```
If you prefer Maven Exec Plugin, you can add it to the build and run with `-Dexec.mainClass=...`. As-is, the plugin is not configured in this project.

## Design highlights

- Separation of concerns
  - Order types encapsulate order-specific behavior.
  - EmailService encapsulates email responsibilities.
  - OrderProcessor coordinates high-level workflow.

- Composition over conditionals
  - Adding new order types: implement the Order interface (or provide a lambda in the functional version).
  - Adding cross-cutting behavior: wrap an existing Order with a decorator (e.g., EmailConfirmationOrder).

- Functional flexibility
  - In the last version, Order is a functional interface, allowing concise lambdas and easy composition with decorators or other higher-order functions.

## Clean code and SOLID practices demonstrated

- Single Responsibility Principle (SRP)
  - Each class has one reason to change: order variants handle processing; the email component handles email; the processor orchestrates.

- Open/Closed Principle (OCP)
  - New order types can be added by creating new implementations without modifying existing processing logic.
  - New cross-cutting behaviors (e.g., confirmations, logging) are added via decorators rather than conditionals.

- Liskov Substitution Principle (LSP)
  - Any Order implementation (class or lambda in the functional version) can be substituted wherever an Order is expected, preserving behavior contracts.

- Interface Segregation Principle (ISP)
  - The Order abstraction is minimal and focused; clients depend only on what they use.

- Dependency Inversion Principle (DIP)
  - High-level modules (processors/decorators) depend on abstractions (Order) and on services passed in (e.g., EmailService), enabling loose coupling and easier testing.

- Composition over inheritance
  - Behavior is composed (decorator, service injection) rather than hard-coded with conditionals or inheritance chains.

- Elimination of magic numbers/flags
  - Replaces primitive flags and type codes with explicit types/abstractions, improving readability and safety.

- Testability and replaceability
  - Services are injected and behaviors are defined via interfaces/functional interfaces, enabling mocks/stubs and fast unit tests.

- Side-effect isolation
  - IO concerns (e.g., email sending) are isolated in dedicated services, making domain logic clearer and easier to reason about.

- Progressive disclosure of complexity
  - Each refactor incrementally improves structure, making the learning path clear from imperative to OO to decorator to functional.

## Extending the system

- New order type (all versions from “better” onward)
  - Create a new class implementing the Order interface, or provide a lambda (functional version).
  - No changes needed to existing processor logic.

- New cross-cutting behavior
  - Create a new decorator that implements Order, wraps another Order, and adds extra behavior before/after delegation.

## Notes

- The final version’s package is named `reafactor_better_functional` (intent is “refactor”). Keep this in mind for run commands and imports.
- Some earlier versions do not include a `Main` class; they are intended to illustrate refactoring direction rather than be executed directly.
