# Copilot Instructions for common-shell

## Build, Test, and Lint Commands

### Build the project
```bash
mvn clean install
```

### Run all tests
```bash
mvn test
```

### Run a single test class
```bash
mvn test -Dtest=ShellTest
```

### Run a single test method
```bash
mvn test -Dtest=ShellTest#testRunCommand
```

### Build without running tests
```bash
mvn clean package -DskipTests
```

### Check code quality with SonarCloud
The CI/CD pipeline automatically runs CodeQL and uploads coverage to SonarCloud. For details, see `.github/workflows/`.

## High-Level Architecture

The `common-shell` library provides a Java framework for building interactive command-line interfaces (CLIs) with minimal boilerplate. The architecture centers on:

1. **Shell** - The main orchestrator that manages the command loop, handles user input/output, and coordinates command execution.

2. **Command Registration** - Commands are discovered via reflection. Methods annotated with `@Command` are automatically registered into a dictionary when the command handler object is passed to the Shell.

3. **Command Dictionary** - A registry that maps command names and shortcuts to `ShellCommand` objects, validates uniqueness across all registered commands, and parses user input into per-invocation command copies.

4. **ShellCommand** - Wraps a reflective `Method` reference along with metadata (name, description, shortcut), the registered handler instance, and parameter information.

5. **Input/Output Providers** - Abstract the I/O layer via `InputProvider` and `OutputProvider` interfaces. Default implementations exist for console-based interaction.

6. **Shell Injection** - The `ShellInject` interface allows handler objects to receive a reference to the Shell instance for scenarios where commands need to control the Shell's behavior.

### Package Organization
- `command/` - Command registration, discovery, and execution
- `command/annotation/` - `@Command` and `@CommandParameter` annotations used to declare CLI commands
- `io/` - Input/output abstraction layer
- `exception/` - Custom exception types
- `util/` - Utilities like `Preconditions`, `StopWatch`

## Key Conventions

### Command Annotation Patterns

All CLI commands must be annotated with `@Command` on a public method:
```java
@Command(name = "list-users", description = "Display all users")
public void listUsers() { ... }
```

- **Command naming**: Use kebab-case for multi-word commands (e.g., `list-users`, `select-user`)
- **Default shortcut generation**: If no `name` and `shortcut` are provided, the Shell auto-generates the shortcut by taking the first letter of each word in the method name. Example: `selectUser()` → shortcut `su`

### Method Parameter Annotation

Parameters of `@Command` methods must be annotated with `@CommandParameter`:
```java
@Command
public void filterBy(
    @CommandParameter(name = "user-role", description = "Role to filter by") String role,
    @CommandParameter(name = "active-only") boolean activeOnly
) { ... }
```

- **Parameter naming convention**: Use kebab-case that is short and descriptive to users. Recommendations: `"number-of-nodes"`, `"user-login"`, `"coefficients"`.
- The `description` attribute is optional. Name and description are propagated into command metadata and are visible in `help`.
- Parsed parameter values must not be stored back into the registered command metadata; keep registry state immutable across invocations.

### Command Lifecycle and Parsing

- Command handlers are registered once and then reused for invocation; do not rely on per-call re-instantiation.
- Auto-generated shortcuts must remain globally unique across built-ins and user-provided commands.
- Command parsing supports repeated whitespace plus single-quoted and double-quoted arguments.
- Unterminated quoted arguments should fail explicitly.

### Copyright and License Headers

All source files must include the Apache 2.0 license header at the top:
```java
/*
 * Copyright 2022 Martin Absmeier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * ...
 */
```

### Testing Conventions

- Test classes use the suffix `Test` (e.g., `ShellTest`, `ShellCommandTest`)
- Tests are located in `src/test/java` mirroring the source structure
- Use JUnit 5, Mockito for mocking, and Awaitility for async testing
- All new features require corresponding unit tests (see CONTRIBUTING.md)

### Lombok Usage

The project uses Lombok to reduce boilerplate. Common annotations:
- `@Getter` - Auto-generates getter methods
- `@Setter` - Auto-generates setter methods
- `@Builder` - Generates builder pattern implementation
- See `ShellCommand` and `Shell` classes for examples

### Java Version and Compiler Settings

- **Target Java version**: 21 (pom.xml: `<java.release>21</java.release>`)
- **Source compatibility**: 11 (for javadoc generation)
- **Compiler warnings**: The POM enables `showDeprecation` to flag deprecated usage
- Prefer modern JDK APIs already in use in the codebase, such as `Path.of(...)`, `String.repeat(...)`, `String.formatted(...)`, and `Stream.toList()` where they improve clarity.

### Zero Dependencies Policy

The library maintains **zero runtime dependencies** — only the JRE is required. This is intentional and a key design principle. Lombok is `provided` only and must not become a runtime dependency. All test dependencies are in `<scope>test</scope>`.

### Documentation

- Use Javadoc comments (`/** ... */`) on public classes and methods
- Document the purpose and behavior, especially for framework methods
- See existing classes like `Shell`, `ShellCommand`, and `ShellFactory` for style examples
