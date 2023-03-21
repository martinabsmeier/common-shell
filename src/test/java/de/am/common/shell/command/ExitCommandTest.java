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
import de.am.common.shell.io.InputProvider;
import de.am.common.shell.io.OutputProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit test cases of {@link ExitCommand} class:
 *
 * @author Martin Absmeier
 */
class ExitCommandTest {

    private ExitCommand command;
    InputProvider inputProvider;
    OutputProvider outputProvider;
    private Shell shell;

    @BeforeEach
    void setUp() {
        inputProvider = mock(InputProvider.class);
        outputProvider = mock(OutputProvider.class);
        shell = mock(Shell.class);

        command = new ExitCommand();
        command.setShell(shell);
    }

    @Test
    void builder() {
        command = new ExitCommand();
        assertNotNull(command, "We expect a ExitCommand instance.");
    }

    @Test
    void exit() {
        doCallRealMethod().when(shell).setShutdown(anyBoolean());
        when(shell.isShutdown()).thenCallRealMethod();
        when(shell.getInputProvider()).thenReturn(inputProvider);
        when(shell.getOutputProvider()).thenReturn(outputProvider);
        doNothing().when(shell).shutdown();

        command.exit();
        assertTrue(shell.isShutdown());
        verify(inputProvider, times(1)).exit();
        verify(outputProvider, times(1)).exit();
    }
}