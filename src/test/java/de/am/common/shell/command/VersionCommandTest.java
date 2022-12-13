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
 * JUnit test cases of {@link VersionCommand} class:
 *
 * @author Martin Absmeier
 */
class VersionCommandTest {

    private VersionCommand command;
    private OutputProvider outputProvider;

    @BeforeEach
    void setUp() {
        command = new VersionCommand();
        outputProvider = mock(OutputProvider.class);
        Shell shell = ShellFactory.createShell(ShellConfig.builder().outputProvider(outputProvider).build());
        command.setShell(shell);
    }

    @Test
    void builder() {
        command = new VersionCommand();
        assertNotNull(command, "We expect a VersionCommand instance.");
    }

    @Test
    void info() {
        command.info();
        verify(outputProvider, times(1)).println(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }
}