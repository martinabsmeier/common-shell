# AGENTS — Anleitung für AI-Coding-Agenten

Kurz, handlungsorientiert und auf dieses Repo zugeschnitten. Ziel: schnelle Einarbeitung von automatischen Code-Änderungs- oder Analyse-Agenten.

Checklist (Was du als Agent zuerst tun solltest)
- Öffne `pom.xml`, `README.md` und `src/main/java/de/am/common/shell/*` zum Überblick.
- Starte mit `mvn test` (oder Einzeltests), um die Basisbuild-Umgebung zu prüfen.
- Suche nach `@Command`-annotierten Methoden (siehe Pattern unten) — das ist der wichtigste Erweiterungspunkt.

1) Big picture — Architektur (Kurz)
- Core: `Shell` ist die Laufzeit-Engine (Eingabe-Schleife, Dispatch). Datei: `src/main/java/de/am/common/shell/Shell.java`.
- Discovery: `ShellFactory` scannt Handler-Objekte nach `@Command`-Methoden, validiert Namen/Shortcuts global und baut die `ShellCommand`-Registry. Datei: `ShellFactory.java`.
- Command model: `ShellCommand` + `ShellCommandParameter` kapseln Method-Reference, Handler-Instanz, Parameter-Metadaten und Konvertierungslogik (`getTypedParameters()`). Datei: `command/ShellCommand.java`.
- Routing: `ShellCommandDictionary` tokenisiert die Raw-User-Input-Zeile, matched das Kommando und erzeugt pro Aufruf eine geparste Command-Kopie. Datei: `command/ShellCommandDictionary.java`.
- IO abstraction: `InputProvider` / `OutputProvider` mit Default-Implementierungen (`DefaultInputProvider`, `DefaultOutputProvider`) — verändere hier, wenn du interaktive Tests oder non-interactive runs simulierst.

2) Wo Erweiterungen hingelegt werden / wie man neue Commands hinzufügt
- Erstelle ein öffentliches POJO-Handler-Objekt mit public Methoden, die `@Command` verwenden. Beispiel: `command/HelpCommand.java`.
- Parameter: markiere jede Methodeingabe mit `@CommandParameter(name = "kebab-case")`; diese Metadaten werden in `help` und in der Command-Registry sichtbar.
- Wenn dein Handler Zugriff auf die Shell braucht, implementiere `ShellInject` und die Shell wird gesetzt (siehe `ShellInject.java`).
- Shortcut-Generation: Wenn `@Command` kein `name`/`shortcut` setzt, generiert `ShellFactory` automatisch ein Shortcut (erste Buchstaben der Wörter). Diese Shortcuts müssen global eindeutig sein; Konflikte schlagen beim Shell-Bau fehl.

3) Typkonvertierung & Reflection-Fallen
- `ShellCommand` konvertiert String-Parameter zu Java-Typen. Schau `getTypedParameters()` an — Agenten müssen passende string-zu-typ Konvertierungen respektieren (z.B. `boolean`, `int`, `String[]`).
- Handler werden pro Kommando **nicht** mehr neu instanziiert: `Shell` ruft die registrierte Handler-Instanz direkt auf. Zustandsbehaftete Commands bleiben daher zwischen Aufrufen erhalten.
- `ShellCommandDictionary` mutiert registrierte Parameter-Metadaten nicht; pro Aufruf werden neue Parameterobjekte mit den geparsten Werten erzeugt.
- Verändere diese Logik nur, wenn du Tests anpasst; bestehende Unit-Tests erwarten die aktuelle Konvertierung.

4) Parsing-Verhalten
- Argumente unterstützen mehrfachen Whitespace sowie einfache und doppelte Quotes, z.B. `cmd "value 1" 'value 2'`.
- Nicht geschlossene Quotes liefern eine `IllegalArgumentException`.
- Die Anzahl der geparsten Parameter muss exakt zur Methodensignatur passen.

5) Entwickler-Workflows / Build & Tests
- Voller Build: `mvn clean install`
- Alle Tests: `mvn test`
- Einzelne Testklasse: `mvn test -Dtest=ShellTest`
- Einzelne Testmethode: `mvn test -Dtest=ShellTest#testRunCommand`
- Paket ohne Tests: `mvn clean package -DskipTests`
- CI/Qualität: Sonar/CodeQL laufen in CI (siehe `.github/workflows/`).

6) Projekt-spezifische Konventionen (wichtig für agenten)
- Java-Version: target Java 21 (siehe `pom.xml`), Quellkompatibilität 11 für javadoc.
- Zero-runtime-deps: Bibliothek beabsichtigt keine Laufzeit-Dependencies — vermeide neue Runtime-Abhängigkeiten.
- Lombok ist als `provided` eingebunden; es ist kein Runtime-Bestandteil des Artefakts.
- Copyright/Header: Java-Quellfiles enthalten Apache-2.0-Header — wenn du neue Java-Dateien erzeugst, füge den Header hinzu.
- Lombok wird verwendet (z.B. `@Getter`, `@Builder`) — erzeugte Klassen sollten Lobok-Annotationen konsistent nutzen, wenn passend.

7) Nützliche Code-Orte / Beispiele (konkret)
- Command discovery: `src/main/java/de/am/common/shell/ShellFactory.java`
- Shell loop & dispatch: `src/main/java/de/am/common/shell/Shell.java`
- Command wrapper & params: `src/main/java/de/am/common/shell/command/ShellCommand.java`
- Dictionary & matching: `src/main/java/de/am/common/shell/command/ShellCommandDictionary.java`
- Built-in commands: `src/main/java/de/am/common/shell/command/*` (Help, Exit, Version, Logging, DisplayTime, ShowException)
- IO hooks: `src/main/java/de/am/common/shell/io/*`
- Small utils: `src/main/java/de/am/common/shell/util/Preconditions.java`, `StopWatch.java`

8) Tests & Assertions to run before proposing changes
- Führe `mvn test` lokal. Prüfe surefire-Reports in `target/surefire-reports/` wenn etwas fehlschlägt.
- Wenn du Discovery, Parsing, Shortcuts oder Parameter-Metadaten änderst, füge/aktualisiere Unit-Tests unter `src/test/java` entsprechend (Repo hat viele existierende Command-Tests).

9) Integration-Punkte / Risiken
- `DefaultOutputProvider` kann Logging in Dateien aktivieren (`enableLogging`) — Änderungen dort beeinflussen Persistenz von Shell-Ausgaben.
- `VersionCommand` liest `application.properties` aus Ressourcen; Änderungen an Properties/Resource-Lokation beeinflussen Info-Command.
- Änderungen an Parameter-Konvertierung oder Tokenisierung beeinflussen alle Cmd-Tests; handle rückwärtskompatibel.
- Doppelte Command-Namen oder Shortcuts schlagen jetzt früh in `ShellFactory.createShell(...)` fehl; beachte das auch in Tests.

10) Short examples für Agent-Aktionen
- Add command: erstelle `src/main/java/de/am/common/shell/command/MyCmd.java` mit
  - public method annotated `@Command(name="do-thing")`
  - Parameteren mit `@CommandParameter(name="arg-name")`
  - Optional: implement `ShellInject` to access shell
  - Achte auf global eindeutigen `shortcut`, falls du ihn manuell setzt
- Change IO for tests: ersetzen `ShellConfig`-Default `DefaultInputProvider` mit Mock-InputProvider in Tests.

11) Wo Agent-Logs und Resultate ablegen
- Nutze `target/` für temporäre Artefakte; schreib keine persistenten Logs im Repo-Root.

Weitere Referenzen
- README.md — Projektbeschreibung und Ziel.
- pom.xml — Java-Release, Dependencies, Test-Plugins.

Ende.
