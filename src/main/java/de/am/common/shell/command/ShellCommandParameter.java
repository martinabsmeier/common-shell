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

import de.am.common.shell.command.annotation.Command;
import de.am.common.shell.command.annotation.CommandParameter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.isNull;

/**
 * {@code ShellCommandParameter} encapsulates the parameters of a method annotated with {@link Command} annotation, the
 * parameters must be annotated with {@link CommandParameter}.
 *
 * @author Martin Absmeier
 */
public class ShellCommandParameter {
    @Getter
    private final int index;
    @Getter
    private final String name;
    @Getter
    @Setter
    private String value;
    @Getter
    private final Class<?> type;

    /**
     * Creates a new instance of {@link ShellCommandParameter} class.
     *
     * @param index the index of the parameter
     * @param name  the name of the parameter
     * @param value the value of the parameter
     * @param type  the type of the parameter
     */
    @Builder
    public ShellCommandParameter(int index, String name, String value, Class<?> type) {
        this.index = index;
        this.name = isNull(name) ? "" : name;
        this.value = isNull(value) ? "" : value;
        this.type = type;
    }
}