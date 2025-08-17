# Clean Code Exercise — Progressive Refactoring Journey

> A hands-on demonstration of transforming legacy code into maintainable, extensible architecture through 7 progressive refactoring stages.

This project showcases the evolution from procedural "order processing" code with magic numbers and boolean flags to a modern, functional, and highly composable system using latest Java features.

## 🚀 Quick Start

```bash
# Build the project
mvn clean package -DskipTests

# Run the most advanced version (v7)
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics.Main
```

## 📈 Refactoring Progression

| Version | Name | Key Improvement |
|---------|------|----------------|
| v1 | `original` | Baseline: procedural code with magic numbers |
| v2 | `refactor_good` | Enums replace magic numbers, better naming |
| v3 | `refactor_better` | Strategy pattern + dependency injection |
| v4 | `refactor_better_with_decorator` | Decorator pattern for cross-cutting concerns |
| v5 | `refactor_better_with_decorator_functional` | Functional interfaces + higher-order functions |
| v6 | `refactor_better_with_decorator_functional_with_java_latest_features` | Modern Java: records, virtual threads, adapters |
| v7 | `refactor_better_with_decorator_functional_with_java_latest_features_and_generics` | **Recommended**: Generic utilities, type-safe pipelines |

## 🎯 Learning Objectives by Version

Each version demonstrates specific clean code principles and design patterns:

### v1: Original (The Problem)
**Anti-patterns demonstrated:**
- Magic numbers (`1`, `2` for order types)
- Boolean flags for cross-cutting concerns
- Tight coupling between core logic and email
- Violates SRP and OCP

```java
processOrder("Alice", 1, true); // What do 1 and true mean?
```

### v2: Good (Basic Cleanup)
**Improvements:**
- Enums replace magic numbers
- Better method naming and structure
- Still conditional-driven but more readable

```java
processOrder("Alice", OrderType.REGULAR, true); // Clearer intent
```

### v3: Better (Strategy Pattern)
**Design patterns introduced:**
- Strategy pattern via `Order` interface
- Dependency injection for collaborators
- Eliminates conditional logic

```java
Order order = new RegularOrder();
processor.process(order, "Alice"); // Polymorphic behavior
```

### v4: Better with Decorator (Separation of Concerns)
**Design patterns added:**
- Decorator pattern for cross-cutting concerns
- Clean separation: core logic vs. email behavior
- Open/Closed Principle compliance

```java
Order order = new EmailConfirmationOrder(new RegularOrder(), emailService);
// Composable behaviors without modifying existing classes
```

### v5: Decorator Functional (Higher-Order Functions)
**Functional programming introduced:**
- `@FunctionalInterface` enables lambda composition
- Higher-order functions replace decorator classes
- Fluent composition with `andThen`

```java
Order order = Order.of(name -> System.out.println("Processing " + name))
    .andThen(emailService::sendConfirmation);
// Functional composition without class explosion
```

### v6: Modern Java Features (Domain-Rich Architecture)
**Modern Java capabilities:**
- Records for immutable data (`Customer`)
- Virtual threads for async operations
- Factory pattern for runtime strategy selection
- Adapter pattern (`Order` ↔ `RichOrder`)
- Registry pattern for declarative pipelines

```java
// Rich domain objects
var customer = new Customer("Alice", "alice@example.com");
var pipeline = Orders.logged(OrderFactory.from(REGULAR), System.out, "AUDIT")
    .withEmail(emailService);

// Async email with virtual threads
emailService.sendConfirmationAsync(customer.name());
```

### v7: Generics & Type Safety (Recommended) ⭐
**Advanced composition utilities:**
- Generic `DecoratorRegistries<K, T>` eliminate duplication
- Type-safe pipeline assembly with `Pipelines` utility
- DRY principle applied to both simple and rich flows

```java
// Declarative, type-safe pipeline composition
var registries = DecoratorRegistries.simple(System.out, emailSender);
var pipeline = Pipelines.loggingTimingEmail(
    OrderFactory.from(REGULAR),
    registries,
    LOGGING, TIMING, EMAIL_ASYNC
);
// Compile-time safety + runtime flexibility
```

## 🏆 Why v7 is the Recommended Approach

**Combines the best of all worlds:**
- **Strategy/Decorator clarity** (v3/v4) + **Functional ergonomics** (v5) + **Modern Java** (v6) + **Type safety** (v7)
- **Zero duplication** between simple and rich domain pipelines
- **Future-proof**: adding new behaviors = composing functions, not creating classes
- **Team-friendly**: consistent, discoverable APIs with compile-time safety

**When to use earlier versions:**
- **v3**: Teams preferring traditional OO with limited cross-cutting needs
- **v4**: Explicit decorator classes for tooling/convention requirements  
- **v5**: Functional style without rich domain objects or async needs
- **v6**: Modern features without generic composition utilities

## 🎨 Design Patterns Demonstrated

- **Strategy Pattern**: Pluggable order processing algorithms
- **Decorator Pattern**: Composable cross-cutting behaviors  
- **Factory Pattern**: Runtime strategy selection
- **Adapter Pattern**: Bridge between simple and rich domains
- **Registry Pattern**: Declarative behavior mapping
- **Functional Composition**: Higher-order functions and pipelines

## 📁 Project Structure

```
src/main/java/com/cleancode/exercise/
├── original/                           # v1: Baseline with magic numbers
├── refactor_good/                      # v2: Enums and better naming
├── refactor_better/                    # v3: Strategy pattern + DI
├── refactor_better_with_decorator/     # v4: Decorator pattern
├── refactor_better_with_decorator_functional/  # v5: Functional interfaces
├── refactor_better_with_decorator_functional_with_java_latest_features/  # v6: Modern Java
└── refactor_better_with_decorator_functional_with_java_latest_features_and_generics/  # v7: Generics
```

**Runnable versions:** v3, v4, v5, v6, v7 include `Main` classes for demonstration.

## ⚙️ Requirements

- **Java 24+** (leverages latest language features)
- **Maven 3.8+**

## 🔨 Build & Run

```bash
# Build
mvn clean package -DskipTests

# Run specific versions
java -cp target/classes com.cleancode.exercise.refactor_better.Main
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator.Main
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator_functional.Main
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features.Main
java -cp target/classes com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics.Main

# Run tests
mvn test
```

## 🧪 Testing Strategy

Each version includes comprehensive tests demonstrating:
- **Unit testing** of individual components
- **Integration testing** of composed behaviors
- **Mocking** of dependencies (email services)
- **Output verification** for cross-cutting concerns

## 🎯 SOLID Principles Demonstrated

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Extensible without modification
- **Liskov Substitution**: Implementations are interchangeable
- **Interface Segregation**: Focused, cohesive interfaces
- **Dependency Inversion**: Depend on abstractions, not concretions

## 🚀 Extending the System

**Add new order type:**
```java
// v3+: Implement interface
public class PriorityOrder implements Order {
    public void process(String customer) { /* logic */ }
}

// v5+: Use lambda
Order priority = Order.of(name -> System.out.println("Priority: " + name));
```

**Add cross-cutting behavior:**
```java
// v4: Create decorator class
public class AuditOrder implements Order { /* wrapper logic */ }

// v5+: Use higher-order function
Order audited = Orders.logged(baseOrder, System.out, "AUDIT");
```

---

**💡 This codebase serves as a practical guide for:**
- Progressive refactoring techniques
- Modern Java development practices  
- Clean architecture principles
- Functional programming in Java
- Design pattern applications