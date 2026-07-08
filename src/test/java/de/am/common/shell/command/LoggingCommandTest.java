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

import de.am.common.shell.ShellConfig;
import de.am.common.shell.ShellFactory;
import de.am.common.shell.io.DefaultOutputProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * JUnit test cases of {@link LoggingCommand} class.
 *
 * @author Martin Absmeier
 */
class LoggingCommandTest {

    private LoggingCommand command;
    private DefaultOutputProvider outputProvider;

    @BeforeEach
    void setUp() {
        command = new LoggingCommand();
        outputProvider = mock(DefaultOutputProvider.class);
        ShellConfig config = ShellConfig.builder().outputProvider(outputProvider).build();
        command.setShell(ShellFactory.createShell(config));
    }

    @Test
    void builder() {
        command = new LoggingCommand();
        assertNotNull(command, "We expect a LoggingCommand instance.");
    }

    @Test
    void fileNameNull() {
        assertThrows(NullPointerException.class, () -> command.enableLogging(null));
    }

    @Test
    void enableLogging() {
        doNothing().when(outputProvider).enableLogging(eq("logfile.log"));

        String fileName = "logfile.log";
        command.enableLogging(fileName);

        verify(outputProvider).enableLogging(eq("logfile.log"));
    }

    @Test
    void enableLoggingNoExtension() {
        doNothing().when(outputProvider).enableLogging(eq("logfile"));

        String fileName = "logfile";
        command.enableLogging(fileName);

        verify(outputProvider).enableLogging(eq("logfile"));
    }

    @Test
    void enableLoggingRejectsPathTraversal() {
        String fileName = ".." + File.separator + "logfile";

        assertThrows(IllegalArgumentException.class, () -> command.enableLogging(fileName));
    }

    @Test
    void disableLogging() {
        doNothing().when(outputProvider).disableLogging();

        command.disableLogging();

        verify(outputProvider).disableLogging();
    }
}