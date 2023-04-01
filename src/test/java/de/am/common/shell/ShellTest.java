/*
 * Copyright 2023 Martin Absmeier
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

import de.am.common.shell.command.annotation.Command;
import de.am.common.shell.io.DefaultInputProvider;
import de.am.common.shell.io.InputProvider;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.am.common.shell.ShellConstants.ANSI_WHITE_BRIGHT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JUnit test cases of {@link Shell} class.
 *
 * @author Martin Absmeier
 */
class ShellTest {

    private InputProvider inputProvider;
    private Shell shell;

    @BeforeEach
    void setUp() {
        inputProvider = mock(DefaultInputProvider.class);
        ShellConfig config = ShellConfig.builder().inputProvider(inputProvider).build();
        shell = ShellFactory.createShell(config);
    }

    @Test
    void execute() {
        assertNotNull(shell);

        when(inputProvider.readCommand()).thenReturn("version").thenReturn("exit");
        shell.execute();
        assertTrue(true, "We expect no exception");
    }

    @Test
    void executeTimeDisplayEnabled() {
        ShellConfig config = ShellConfig.builder().inputProvider(inputProvider).isTimeDisplayed(true).build();
        shell = ShellFactory.createShell(config);
        assertNotNull(shell);
        assertTrue(shell.isTimeDisplayed());

        when(inputProvider.readCommand()).thenReturn("version").thenReturn("exit");
        shell.execute();
    }

    @Test
    void executeCommandNotFound() {
        assertNotNull(shell);

        when(inputProvider.readCommand()).thenReturn("noCommand").thenReturn("exit");
        shell.execute();
        assertTrue(true, "We expect no exception");
    }

    @Test
    void executeDictionaryException() {
        ShellConfig config = ShellConfig.builder().inputProvider(inputProvider).isTimeDisplayed(true).build();
        shell = ShellFactory.createShell(config, new ExceptionCommand());
        assertNotNull(shell);

        when(inputProvider.readCommand()).thenReturn("throwException").thenReturn("exit");

        shell.execute();
        assertNotNull(shell.getException());
    }

    public static class ExceptionCommand implements ShellInject {
        @Setter
        private Shell shell;
        private boolean isFirstTime = true;

        @Command(name = "throwException")
        public void throwException() {
            if (isFirstTime) {
                isFirstTime = false;
                throw new NullPointerException();
            }
            shell.getOutputProvider().println("Nothing to do.", ANSI_WHITE_BRIGHT);
        }
    }
}