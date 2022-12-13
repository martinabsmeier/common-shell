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

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit test cases of {@link ShellCommand} class.
 *
 * @author Martin Absmeier
 */
class ShellCommandTest {

    private ShellCommand command;
    private Method method;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        method = Integer.class.getMethod("parseInt", String.class, int.class);
        command = ShellCommand.builder()
            .name("Command-1")
            .description("Command-1 description")
            .shortcut("cmd")
            .method(method)
            .parameters(createParameter(2))
            .build();
    }

    @Test
    void getName() {
        assertNotNull(command.getName(), "We expect a name.");
    }

    @Test
    void getDescription() {
        assertNotNull(command.getDescription(), "We expect a description.");
    }

    @Test
    void getShortcut() {
        assertNotNull(command.getShortcut(), "We expect a shortcut.");
    }

    @Test
    void getMethod() {
        assertNotNull(command.getMethod(), "We expect a method.");
    }

    @Test
    void getParameter() {
        assertNotNull(command.getParameters(), "We expect three parameters.");
    }

    @Test
    void getTypedParametersThrowIllegalArgumentException() {
        command = ShellCommand.builder()
            .name("Command-1")
            .description("Command-1 description")
            .shortcut("cmd")
            .method(method)
            .parameters(createParameter(3))
            .build();
        assertThrows(IllegalArgumentException.class, () -> command.getTypedParameters(), "We expect an IllegalArgumentException to be thrown.");
    }

    @Test
    void getTypedParametersNullPointerException() {
        assertThrows(IllegalArgumentException.class, () -> command.getTypedParameters(), "We expect an IllegalArgumentException to be thrown.");
    }

    @Test
    void convertValueToType() throws NoSuchMethodException {
        ShellCommandParameter[] parameters= new ShellCommandParameter[]{
            ShellCommandParameter.builder().index(1).name("param-1").value("string").build(),
            ShellCommandParameter.builder().index(1).name("param-2").value("1").build(),
            ShellCommandParameter.builder().index(1).name("param-3").value("1000").build(),
            ShellCommandParameter.builder().index(1).name("param-4").value("2.50").build(),
            ShellCommandParameter.builder().index(1).name("param-5").value("10.95").build(),
            ShellCommandParameter.builder().index(1).name("param-6").value("true").build()
        };

        Method foo = getClass().getMethod("foo", String.class, Integer.class, Long.class, Double.class, Float.class, Boolean.class);
        command = ShellCommand.builder().name("fooCommand").description("Foo command description")
            .shortcut("fC").method(foo).parameters(parameters).build();

        Object[] typedParams = command.getTypedParameters();
        assertNotNull(typedParams, "We expect typed parameter.");
    }

    // #################################################################################################################
    private ShellCommandParameter[] createParameter(int count) {
        ShellCommandParameter[] parameter = new ShellCommandParameter[count];

        for (int index = 0; index < count; index++) {
            String name = "param-" + index + 1;
            parameter[index] = ShellCommandParameter.builder().index(1).name(name).build();

        }

        return parameter;
    }

    public String foo(String s, Integer i, Long l, Double d, Float f, Boolean b) {
        return s + i + l + d + f + b;
    }
}