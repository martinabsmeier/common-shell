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
package de.am.common.shell.command;

import de.am.common.shell.Shell;
import de.am.common.shell.ShellConfig;
import de.am.common.shell.ShellFactory;
import de.am.common.shell.command.annotation.Command;
import de.am.common.shell.command.annotation.CommandParameter;
import de.am.common.shell.io.OutputProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * JUnit test cases of {@link HelpCommand} class.
 *
 * @author Martin Absmeier
 */
class HelpCommandTest {

    private HelpCommand command;
    private OutputProvider outputProvider;

    @BeforeEach
    void setUp() {
        command = new HelpCommand();
        outputProvider = mock(OutputProvider.class);
        Shell shell = ShellFactory.createShell(ShellConfig.builder().outputProvider(outputProvider).build());
        command.setShell(shell);
    }

    @Test
    void builder() {
        command = new HelpCommand();
        assertNotNull(command, "We expect a HelpCommand instance.");
    }

    @Test
    void help() {
        command.help();
        verify(outputProvider, times(1)).println(anyString(), anyString(), anyString());
    }

    @Test
    void helpUsesAnnotatedParameterName() {
        Shell shell = ShellFactory.createShell(
            ShellConfig.builder().outputProvider(outputProvider).build(),
            new ParameterizedHelpCommand()
        );
        command.setShell(shell);

        command.help();

        verify(outputProvider).println(
            eq("{0} | {1} | {2} | {3}"),
            anyString(),
            contains("annotated-help"),
            anyString(),
            contains("(String file-name)"),
            anyString()
        );
    }

    private static class ParameterizedHelpCommand {

        @Command(name = "annotated-help")
        public void annotated(@CommandParameter(name = "file-name", description = "Help parameter.") String fileName) {
            // nothing to do
        }
    }
}