[![Java CI with Maven](https://github.com/martinabsmeier/common-shell/actions/workflows/maven.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/maven.yml)
[![CodeQL](https://github.com/martinabsmeier/common-shell/actions/workflows/codeql.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/codeql.yml)
[![Coverage](https://github.com/martinabsmeier/common-shell/actions/workflows/coverage.yml/badge.svg)](https://github.com/martinabsmeier/common-shell/actions/workflows/coverage.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=martinabsmeier_common-shell&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=martinabsmeier_common-shell)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)

# Shell library
* Zero dependencies only JRE is required.
* Provides annotations to create a interactive command-line user interfaces.
* Create commands and call a specific method for execution
* No need to parse arguments

## Dependencies
The *shell* library has zero dependencies at runtime.

## Getting Started
There are three options to use the shell library.
1. Download the source code from GitHub and build the project yourself
2Download the finished artifact from maven central

### First option
- Install a JDK found at [Adoptium](https://adoptium.net/de/).
- Install the maven build tool found at [maven](https://maven.apache.org)
- Clone the repository from GitHub found at [shell library](https://github.com/martinabsmeier/common-shell).

#### Clone repository
```
git clone git@github.com:martinabsmeier/common-shell.git
```

#### Building library
```
mvn clean install
```

#### Running Tests
```
mvn test
```

### Second option
If you use maven as build system add the following dependency to your pom.xml file.
```xml
<dependency>
    <groupId>de.am.common</groupId>
    <artifactId>shell</artifactId>
    <version>1.0.1</version>
</dependency>
```

### User guide
Instructions for using your project. Ways to run the program, how to include it in another project, etc.

## Releases
* For available releases, see the [repository release list](https://github.com/martinabsmeier/common-shell/releases).
* For available tags, see the [repository tag list](https://github.com/martinabsmeier/common-shell/tags).

## Versioning
This project uses [Semantic Versioning](http://semver.org/).

## Contributing
Provide details about how people can contribute to your project. If you have a contributing guide, mention it here. e.g.:
We encourage public contributions! Please review [CONTRIBUTING](CONTRIBUTING.md) for details on our code of conduct and development process.

## License
This project is licensed under the Apache License - see [LICENSE](LICENSE) file for details.

## Authors
* **[Martin Absmeier](https://github.com/martinabsmeier)** - *Initial work*
* Also see the list of [contributors](https://github.com/martinabsmeier/common-shell/contributors) who participated in this project.
