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
package de.am.common.shell.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit test cases of {@link DefaultInputProvider} class.
 *
 * @author Martin Absmeier
 */
class DefaultInputProviderTest {

    private DefaultInputProvider provider;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        scanner = mock(Scanner.class);
        provider = DefaultInputProvider.builder().scanner(scanner).build();
    }

    @Test
    void readCommand() {
        String expected = "help";
        when(scanner.nextLine()).thenReturn(expected);

        String actual = provider.readCommand();
        assertEquals(expected, actual, "Commands not equal.");
    }

    @Test
    void exit() {
        provider.exit();
        verify(scanner, times(1)).close();
    }
}