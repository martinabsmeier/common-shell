[![Java CI with Maven](https://github.com/martinabsmeier/common-shell/actions/workflows/maven.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/maven.yml)
[![CodeQL](https://github.com/martinabsmeier/common-shell/actions/workflows/codeql.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/codeql.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=martinabsmeier_common-shell&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=martinabsmeier_common-shell)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)

# common-shell

`common-shell` is a small Java library for building interactive command-line shells with minimal boilerplate.

It discovers command methods via annotations, parses command arguments, provides built-in helper commands, and runs on plain Java without runtime dependencies.

## Features

- Zero runtime dependencies
- Annotation-based command registration
- Reused command handler instances across invocations
- Auto-generated command shortcuts
- Built-in commands for `help`, `exit`, `version`, exception display, logging, and execution time display
- Typed parameter conversion for common Java types
- Quoted argument parsing for commands such as `"hello world"`
- Configurable limits for command length and log entry length
- Safer defaults for exception display and log file handling
- Clean console output without logging prefixes

## Requirements

- Java 21 for build and runtime
- Maven 3.9+

## Build and test

```shell
mvn clean install
```

```shell
mvn test
```

## Quick start

```java
import de.am.common.shell.Shell;
import de.am.common.shell.ShellFactory;
import de.am.common.shell.command.annotation.Command;
import de.am.common.shell.command.annotation.CommandParameter;

public class DemoCommands {

    @Command(name = "greet", description = "Print a greeting")
    public String greet(@CommandParameter(name = "name") String name) {
        return "Hello " + name + "!";
    }

    public static void main(String[] args) {
        Shell shell = ShellFactory.createShell("> ", "Demo", new DemoCommands());
        shell.execute();
    }
}
```

Example session:

```text
Demo> greet Martin
Hello Martin!
Demo> help
Demo> exit
Shutdown shell...
```

## Declaring commands

Commands are public methods annotated with `@Command`.

```java
@Command(name = "add-user", shortcut = "au", description = "Create a user")
public void addUser(
    @CommandParameter(name = "user-name", description = "Display name of the user") String userName,
    @CommandParameter(name = "active") boolean active
) {
    // ...
}
```

### Naming rules

- Prefer **kebab-case** command names such as `add-user`
- Prefer **kebab-case** parameter names such as `user-name`
- If `shortcut` is omitted, a shortcut is generated from the method name
- Command names and shortcuts must be globally unique, including built-in commands

## Built-in commands

The shell automatically registers these commands:

| Command | Purpose |
|---|---|
| `help` | Show all registered commands |
| `exit` | Stop the shell loop |
| `version` | Show library/runtime version information |
| `showException` / `sE` | Show the last exception type, or full details if enabled |
| `enableLogging` / `eL` | Write shell output to a log file |
| `disableLogging` / `dL` | Stop writing shell output to a log file |
| `enableTimeDisplay` | Show command runtime |
| `disableTimeDisplay` | Hide command runtime |

## Configuration

Use `ShellConfig` to customize shell behavior.

```java
import de.am.common.shell.Shell;
import de.am.common.shell.ShellConfig;
import de.am.common.shell.ShellFactory;

ShellConfig config = ShellConfig.builder()
    .appName("Demo")
    .prompt("> ")
    .isTimeDisplayed(false)
    .isExceptionDetailsDisplayed(false)
    .maxCommandLength(4096)
    .maxLogEntryLength(16384)
    .build();

Shell shell = ShellFactory.createShell(config, new DemoCommands());
```

### Important defaults

- Exception details are hidden by default; `showException` only exposes the exception type unless detailed output is explicitly enabled
- Log files must be simple file names in the current working directory
- Log file targets that are symbolic links or non-regular files are rejected
- Excessively long commands are rejected
- End-of-input (EOF) stops the shell cleanly

## Accessing the running shell

If a command handler needs direct access to the shell, implement `ShellInject`.

```java
import de.am.common.shell.Shell;
import de.am.common.shell.ShellInject;
import de.am.common.shell.command.annotation.Command;

public class AdminCommands implements ShellInject {

    private Shell shell;

    @Override
    public void setShell(Shell shell) {
        this.shell = shell;
    }

    @Command(name = "stop")
    public void stop() {
        shell.shutdown();
    }
}
```

`shutdown()` stops the shell loop only. It does not terminate the host JVM with `System.exit(...)`.

## Parameter handling

The shell converts string arguments to the method parameter types it supports, including:

- `String`
- `int` / `Integer`
- `long` / `Long`
- `double` / `Double`
- `float` / `Float`
- `boolean` / `Boolean`

Boolean values are parsed strictly: only `true` and `false` are accepted.

Arguments support repeated whitespace and quoted values:

```text
say "hello world"
move 'my file.txt' target
```

## Logging

`enableLogging` writes shell output to a local `.log` file in the current working directory.

Examples:

```text
enableLogging shell.log
enableLogging session
disableLogging
```

When no `.log` suffix is provided, it is appended automatically.

## CI and quality

GitHub Actions workflows in this repository currently provide:

- Maven build and test verification
- JaCoCo coverage report generation as workflow artifacts
- CodeQL analysis
- SonarCloud analysis

## Project status

- Releases: [GitHub Releases](https://github.com/martinabsmeier/common-shell/releases)
- Tags: [GitHub Tags](https://github.com/martinabsmeier/common-shell/tags)
- Versioning: [Semantic Versioning](https://semver.org/)

## Contributing

Contributions are welcome. Please review:

- [CONTRIBUTING.md](CONTRIBUTING.md)
- [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
- [SECURITY.md](SECURITY.md)

## License

This project is licensed under the Apache License 2.0. See [LICENSE](LICENSE) for details.
