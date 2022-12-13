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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * JUnit test cases of {@link ShellCommandParameter} class;
 *
 * @author Martin Absmeier
 */
class ShellCommandParameterTest {

    private ShellCommandParameter commandParam;

    @BeforeEach
    void setUp() {
        commandParam = ShellCommandParameter.builder().build();
    }

    @Test
    void builderEmpty() {
        assertNotNull(commandParam, "An instance is expected!");

        assertEquals("", commandParam.getName(), "Empty name is expected!");
        assertNull(commandParam.getType(), "NULL type is expected!");
    }

    @Test
    void builder() {
        commandParam = ShellCommandParameter.builder().index(1).name("cmdName").type(Integer.class).build();
        assertNotNull(commandParam, "An instance is expected!");

        assertEquals(1, commandParam.getIndex(), "Value '1' is expected!");
        assertEquals("cmdName", commandParam.getName(), "Value 'cmdName' is expected!");
        assertEquals(Integer.class, commandParam.getType(), "Integer type is expected!");
    }

    @Test
    void setValue() {
        commandParam = ShellCommandParameter.builder().build();
        commandParam.setValue("foo");

        assertEquals("foo", commandParam.getValue(), "Value 'foo' is expected!");
    }
}