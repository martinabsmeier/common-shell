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
import de.am.common.shell.io.OutputProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * JUnit test cases of {@link ShowExceptionCommand} class.
 *
 * @author Martin Absmeier
 */
class ShowExceptionCommandTest {

    private ShowExceptionCommand command;
    private OutputProvider outputProvider;

    @BeforeEach
    void setUp() {
        command = new ShowExceptionCommand();
        outputProvider = mock(OutputProvider.class);
        Shell shell = ShellFactory.createShell(ShellConfig.builder().outputProvider(outputProvider).build());
        command.setShell(shell);
    }

    @Test
    void builder() {
        command = new ShowExceptionCommand();
        assertNotNull(command, "We expect a ShowExceptionCommand instance.");
    }

    @Test
    void showExceptionNull() {
        command.showException();
        verify(outputProvider, times(1)).println(anyString(), anyString(), anyString());
    }

    @Test
    void showException() {
        Shell shell = ShellFactory.createShell(ShellConfig.builder().outputProvider(outputProvider).build());
        RuntimeException exception = new RuntimeException("JUnit test.");
        shell.setException(exception);
        command.setShell(shell);

        command.showException();
        int invocations = exception.getStackTrace().length + 1;
        verify(outputProvider, times(invocations)).println(anyString(), anyString(), anyString());
    }
}