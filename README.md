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
| v7 | `refactor_better_with_decorator_functional_with_java_latest_features_and_generics` | **Production-Ready**: Generic utilities, type-safe pipelines |

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

### v7: Generics & Type Safety (Production-Ready) ⭐
**Enterprise-grade composition utilities:**
- Generic `DecoratorRegistries<K, T>` eliminate all duplication
- Type-safe pipeline assembly with `Pipelines` utility class
- DRY principle applied across simple and rich domain flows
- Bounded generics ensure compile-time safety

```java
// Declarative, type-safe pipeline composition
var registries = DecoratorRegistries.simple(System.out, emailSender);
var pipeline = Pipelines.loggingTimingEmail(
    OrderFactory.from(REGULAR),
    registries,
    LOGGING, TIMING, EMAIL_ASYNC
);
// Compile-time safety + runtime flexibility + zero duplication

// Generic utilities work across domains
var richRegistries = DecoratorRegistries.rich(System.out, emailSender);
var richPipeline = Pipelines.loggingRetryEmail(
    OrderAdapters.toRich(baseOrder),
    richRegistries,
    RichDecoratorType.LOGGING, RichDecoratorType.RETRY_3, RichDecoratorType.EMAIL_ASYNC
);
```

## 🏆 Why v7 is the Recommended Approach

### Architectural Excellence
**Combines the best of all worlds:**
- **Strategy/Decorator clarity** (v3/v4) + **Functional ergonomics** (v5) + **Modern Java** (v6) + **Type safety** (v7)
- **Zero duplication** between simple and rich domain pipelines
- **Future-proof**: adding new behaviors = composing functions, not creating classes
- **Enterprise-ready**: type-safe, testable, maintainable at scale

### Technical Advantages
- **Compile-time Safety**: Generic constraints prevent runtime errors
- **Performance**: Virtual threads, immutable data, efficient composition
- **Flexibility**: Runtime configuration with declarative pipeline assembly
- **Maintainability**: DRY principles, clear separation of concerns

### Team Benefits
- **Consistent APIs**: Uniform patterns across domains and use cases
- **Discoverability**: IDE-friendly method names and type hints
- **Onboarding**: Self-documenting code with clear architectural patterns
- **Scalability**: Easy to extend without modifying existing code

### When to Use Earlier Versions
- **v3**: Traditional OO teams, simple requirements, limited cross-cutting needs
- **v4**: Explicit decorator classes required by tooling/conventions
- **v5**: Functional style without rich domain objects or async requirements
- **v6**: Modern Java features without generic composition utilities
- **v7**: **Recommended for production systems** requiring maintainability and extensibility

## 🎨 Design Patterns & Architecture

### Classic Design Patterns
- **Strategy Pattern**: Pluggable order processing algorithms (`Order` interface, `OrderFactory`)
- **Decorator Pattern**: Functional composition of cross-cutting behaviors
- **Factory Pattern**: Runtime strategy selection and object creation
- **Adapter Pattern**: Bridge between simple (`Order`) and rich (`RichOrder`) domains
- **Registry Pattern**: Enum-to-function mapping for declarative composition
- **Template Method**: Generic pipeline assembly algorithms
- **Null Object**: `NONE` decorators for stage skipping

### Architectural Patterns
- **Layered Architecture**: Clear separation of presentation, application, domain, and infrastructure
- **Hexagonal Architecture**: Ports & adapters isolate core domain from external concerns
- **Functional Architecture**: Pure functions, immutable data, higher-order functions
- **Plugin Architecture**: Extensible decorator system with runtime configuration

### Modern Java Features (v6-v7)
- **Records**: Immutable data structures (`Customer`, `DecoratorRegistries`)
- **Virtual Threads**: Lightweight concurrency for async operations
- **Pattern Matching**: Modern switch expressions with exhaustive matching
- **Generics**: Type-safe composition utilities with bounded type parameters
- **Functional Interfaces**: Lambda expressions and method references
- **Sealed Classes**: Controlled inheritance hierarchies (where applicable)

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
**Test coverage:** Each version includes comprehensive unit and integration tests.

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

## 🎯 SOLID Principles & Clean Code Practices

### SOLID Principles
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Extensible without modification via composition
- **Liskov Substitution**: All implementations are interchangeable
- **Interface Segregation**: Focused, single-method interfaces
- **Dependency Inversion**: Depend on abstractions, not concretions

### Clean Code Practices
- **DRY**: Generic utilities eliminate duplication across domains
- **KISS**: Simple interfaces with clear, focused responsibilities
- **YAGNI**: No speculative features, each component serves current needs
- **Composition over Inheritance**: Behavior added through functional composition
- **Immutability**: Records, immutable maps, pure functions
- **Referential Transparency**: Predictable behavior, easier reasoning

### Functional Programming Principles
- **Higher-Order Functions**: Functions that take/return other functions
- **Function Composition**: Declarative behavior chaining
- **Closure Capture**: Lambdas capture configuration parameters
- **Pure Functions**: No side effects in core decorators

## 🚀 Extending the System

### Add New Order Type
```java
// v3+: Traditional implementation
public class PriorityOrder implements Order {
    public void process(String customer) {
        System.out.println("PRIORITY: Processing " + customer);
    }
}

// v5+: Functional approach
Order priority = Order.of(name -> System.out.println("PRIORITY: " + name));

// v6+: Factory integration
// Add to OrderType enum and OrderFactory switch
```

### Add Cross-Cutting Behavior
```java
// v4: Traditional decorator class
public class AuditOrder implements Order {
    private final Order delegate;
    public AuditOrder(Order delegate) { this.delegate = delegate; }
    public void process(String customer) {
        auditLog("Processing: " + customer);
        delegate.process(customer);
    }
}

// v5+: Higher-order function
Order audited = Orders.logged(baseOrder, System.out, "AUDIT");

// v7: Registry integration
map.put(DecoratorType.AUDIT, order -> Orders.logged(order, auditStream, "AUDIT"));
```

### Add New Pipeline Pattern
```java
// v7: Generic pipeline utility
public static <K extends Enum<K>, T> T auditRetryEmail(
    T base, DecoratorRegistries<K, T> regs,
    K auditKey, K retryKey, K emailKey
) {
    return threeStage(base, regs, auditKey, retryKey, emailKey);
}
```

## 🏗️ Architecture Benefits

### Maintainability
- **Clear Separation**: Domain, infrastructure, and cross-cutting concerns isolated
- **Single Responsibility**: Each component has focused purpose
- **Consistent Patterns**: Uniform approach across simple and rich domains

### Extensibility  
- **Open/Closed Compliance**: Add behaviors without modifying existing code
- **Plugin Architecture**: Runtime decorator selection and composition
- **Generic Utilities**: Type-safe extension points for new domains

### Performance
- **Virtual Threads**: Lightweight concurrency for async operations
- **Immutable Data**: Thread-safe, cacheable structures
- **Lazy Evaluation**: Decorators applied only when needed

### Developer Experience
- **Type Safety**: Compile-time guarantees prevent runtime errors
- **IDE Support**: Method references, auto-completion, refactoring
- **Testability**: Pure functions, mockable interfaces, isolated components

---

## 📚 Learning Outcomes

**This codebase demonstrates:**

### Refactoring Techniques
- **Progressive Enhancement**: Step-by-step improvement without breaking changes
- **Legacy Code Transformation**: From procedural to functional architecture
- **Pattern Recognition**: Identifying and applying appropriate design patterns

### Modern Java Mastery
- **Language Evolution**: Leveraging Java 14+ features effectively
- **Functional Programming**: Combining OOP with functional paradigms
- **Concurrency**: Virtual threads and async programming patterns
- **Type System**: Advanced generics and bounded type parameters

### Architecture Skills
- **Clean Architecture**: Dependency inversion and hexagonal patterns
- **Domain Modeling**: Simple vs. rich domain representations
- **Cross-Cutting Concerns**: Elegant handling of logging, timing, email
- **Composition Patterns**: Building complex behavior from simple components

### Professional Practices
- **SOLID Principles**: Real-world application in modern codebases
- **Testing Strategies**: Unit, integration, and behavioral testing
- **Code Organization**: Package structure and dependency management
- **Documentation**: Self-documenting code with clear intent

**💡 Perfect for:**
- Senior developers learning functional programming
- Teams adopting modern Java practices
- Architecture reviews and design discussions
- Training on clean code principles
- Understanding decorator and strategy patterns