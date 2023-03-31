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

import de.am.common.shell.io.DefaultInputProvider;
import de.am.common.shell.io.InputProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        when(inputProvider.readCommand()).thenReturn("version").thenReturn("exit");

        shell.execute();
    }
}