[![Java CI with Maven](https://github.com/martinabsmeier/common-shell/actions/workflows/maven.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/maven.yml)
[![CodeQL](https://github.com/martinabsmeier/common-shell/actions/workflows/codeql.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/codeql.yml)
[![Coverage](https://github.com/martinabsmeier/common-shell/actions/workflows/coverage.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/coverage.yml)

# Shell library
**Annotated Method Shell Command Java Library**  
This is a Java library that allows you to create and process annotated methods as shell commands. It provides a simple 
and easy-to-use interface for defining shell commands as methods in your Java code, and for executing those commands from 
the command line.

## Features
* Provides annotations to create a interactive command-line user interfaces.
* Create commands and call a specific method for execution
* No need to parse arguments
* Handles errors and exceptions

## Dependencies
The *shell* library has zero dependencies.

## Getting Started
### Requirements
* Installed java SDK minimum is version 11 see [Adoptium](https://adoptium.net/de/).
* Installed maven build tool see [Maven](https://maven.apache.org)
* Clone the repository from GitHub.

### Clone the common-shell repository
```shell
git clone git@github.com:martinabsmeier/common-shell.git
```

### Build the common-shell library
```shell
mvn clean install
```

### Run the common-shell JUnit Tests
```shell
mvn test
```

## Usage

### Annotation
* **@Command(name = '', shortcut)**  
  RRsssd
* **@CommandParameter**  
  sddddddf

To use this library, you must first create a class that implements the *ShellInject* interface, this class contains one 
or more methods annotated with *@Command*:

```java
public class MyCommands implements ShellInject {

    private Shell shell;

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    @Command
    public void echo(String message) {
        OutputProvider out = shell.getOutputProvider();
        out.println("Echo -> {0}", message);
    }
}
```
Bla lba


```java

```

## Releases
* For available releases, see the [repository release list](https://github.com/martinabsmeier/common-shell/releases).
* For available tags, see the [repository tag list](https://github.com/martinabsmeier/common-shell/tags).

## Versioning
This project uses [Semantic Versioning](http://semver.org/).

## Contributing
Everyone is invited to further develop the project, be it with ideas for new use cases or with source code.
Please review [CONTRIBUTING](CONTRIBUTING.md) for details on our code of conduct and development process.

## License
This project is licensed under the Apache License - see [LICENSE](LICENSE) file for details.

## Authors
* **[Martin Absmeier](https://github.com/martinabsmeier)** - *Initial work*
* Also see the list of [contributors](https://github.com/martinabsmeier/common-shell/contributors) who participated in this project.
