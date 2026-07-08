/*
 * Copyright 2022 Martin Absmeier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.am.common.shell;

import de.am.common.shell.command.ShellCommand;
import de.am.common.shell.command.ShellCommandParameter;
import de.am.common.shell.command.annotation.Command;
import de.am.common.shell.command.annotation.CommandParameter;
import de.am.common.shell.io.OutputProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.am.common.shell.ShellConstants.DEFAULT_APP_NAME;
import static de.am.common.shell.ShellConstants.DEFAULT_PROMPT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit test cases of {@link ShellFactory} class.
 *
 * @author Martin Absmeier
 */
class ShellFactoryTest {

    @Test
    void createShellAllNull() {
        Shell actual = ShellFactory.createShell(null, null, (Object[]) null);
        assertNotNull(actual, "An instance of the Shell class is expected.");

        String expected = DEFAULT_APP_NAME.concat(DEFAULT_PROMPT);
        assertEquals(expected, actual.getPrompt(), "Prompt does not match the expected prompt.");
    }

    @Test
    void createShellPrompt() {
        String prompt = "=>";
        Shell actual = ShellFactory.createShell(prompt, null, (Object[]) null);
        assertNotNull(actual, "An instance of the Shell class is expected.");

        String expected = DEFAULT_APP_NAME.concat(prompt);
        assertEquals(expected, actual.getPrompt(), "Prompt does not match the expected prompt.");
    }

    @Test
    void createShellAppName() {
        String appName = "myApp";
        ShellConfig config = ShellConfig.builder().appName(appName).build();

        Shell actual = ShellFactory.createShell(config, (Object[]) null);
        assertNotNull(actual, "An instance of the Shell class is expected.");

        String expected = appName.concat(DEFAULT_PROMPT);
        assertEquals(expected, actual.getPrompt(), "Prompt does not match the expected prompt.");
    }

    @Test
    void createShellCommands() {
        ShellConfig config = ShellConfig.builder().build();
        TestCommands handler = new TestCommands();
        Shell actual = ShellFactory.createShell(config, handler);
        assertNotNull(actual, "An instance of the Shell class is expected.");
        assertSame(actual, handler.shell, "Shell should be injected into the registered handler.");

        List<ShellCommand> commands = actual.getDictionary().getCommands();
        assertNotNull(commands, "We expect commands.");
    }

    @Test
    void createShellUsesCommandParameterMetadata() {
        ShellConfig config = ShellConfig.builder().build();
        Shell actual = ShellFactory.createShell(config, new ParameterizedCommands());

        ShellCommand command = actual.getDictionary().getCommands().stream()
            .filter(shellCommand -> "annotated".equals(shellCommand.getName()))
            .findFirst()
            .orElse(null);
        assertNotNull(command, "A command instance is expected.");

        ShellCommandParameter parameter = command.getParameters()[0];
        assertEquals("file-name", parameter.getName(), "Annotated parameter name should be used.");
        assertEquals("A file passed from the shell.", parameter.getDescription(), "Annotated parameter description should be used.");
    }

    @Test
    void createShellRejectsDuplicateCommandAcrossHandlers() {
        ShellConfig config = ShellConfig.builder().build();

        assertThrows(IllegalArgumentException.class, () -> ShellFactory.createShell(config, new DuplicateCommandsOne(), new DuplicateCommandsTwo()));
    }

    @Test
    void createShellRejectsDuplicateIdentifierAgainstStandardCommands() {
        ShellConfig config = ShellConfig.builder().build();

        assertThrows(IllegalArgumentException.class, () -> ShellFactory.createShell(config, new DuplicateExitCommand()));
    }

    // #################################################################################################################

    private static class TestCommands implements ShellInject {
        private Shell shell;

        @Override
        public void setShell(Shell shell) {
            this.shell = shell;
        }

        @Command
        public void commandE() {
            OutputProvider out = shell.getOutputProvider();
            out.println("{}", "Annotated with @Command");
        }

        @Command(shortcut = "cem")
        public void commandEm() {
            OutputProvider out = shell.getOutputProvider();
            out.println("{}", "Annotated with @Command");
        }

        @Command(shortcut = "cemp")
        public void commandEmp() {
            OutputProvider out = shell.getOutputProvider();
            out.println("{}", "Annotated with @Command");
        }
    }

    private static class ParameterizedCommands {

        @Command(name = "annotated")
        public void annotated(@CommandParameter(name = "file-name", description = "A file passed from the shell.") String fileName) {
            // nothing to do
        }
    }

    private static class DuplicateCommandsOne {

        @Command(name = "duplicate-command")
        public void first() {
            // nothing to do
        }
    }

    private static class DuplicateCommandsTwo {

        @Command(name = "duplicate-command")
        public void second() {
            // nothing to do
        }
    }

    private static class DuplicateExitCommand {

        @Command(name = "custom-exit", shortcut = "exit")
        public void exitLike() {
            // nothing to do
        }
    }
}