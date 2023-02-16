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
import de.am.common.shell.io.InputProvider;
import de.am.common.shell.io.OutputProvider;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Permission;

import static de.am.common.shell.ShellConstants.SUCCESSFUL;
import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * JUnit test cases of {@link ExitCommand} class:
 *
 * @author Martin Absmeier
 */
class ExitCommandTest {

    SecurityManager originalSecurityManager;
    private ExitCommand command;
    InputProvider inputProvider;
    OutputProvider outputProvider;
    private Shell shell;

    @BeforeEach
    void setUp() {
        originalSecurityManager = System.getSecurityManager();
        command = new ExitCommand();
        inputProvider = mock(InputProvider.class);
        outputProvider = mock(OutputProvider.class);

        shell = ShellFactory.createShell(ShellConfig.builder().inputProvider(inputProvider).outputProvider(outputProvider).build());
        command.setShell(shell);
    }

    @Test
    void builder() {
        command = new ExitCommand();
        assertNotNull(command, "We expect a ExitCommand instance.");
    }

    @Test
    void exit() {
        try {
            System.setSecurityManager(new NoExitSecurityManager());
            command.exit();
        } catch (ExitException ex) {
            assertTrue(shell.isShutdown());
            verify(inputProvider, times(1)).exit();
            verify(outputProvider, times(1)).exit();
            assertEquals(SUCCESSFUL, ex.getStatus(), "Wrong System.exit() status.");
        } finally {
            System.setSecurityManager(originalSecurityManager);
        }
    }

    // #################################################################################################################
    private class NoExitSecurityManager extends SecurityManager {

        public void checkPermission(final Permission perm) {
            if (nonNull(originalSecurityManager))
                originalSecurityManager.checkPermission(perm);
        }

        @Override
        public void checkPermission(final Permission perm, final Object context) {
            if (nonNull(originalSecurityManager))
                originalSecurityManager.checkPermission(perm, context);
        }

        @Override
        public void checkExit(final int status) {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }

    private static class ExitException extends SecurityException {
        private static final long serialVersionUID = 3830534030825485468L;
        @Getter
        final int status;

        private ExitException(final int status) {
            this.status = status;
        }
    }
}