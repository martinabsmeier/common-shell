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
import de.am.common.shell.exception.ShellException;
import de.am.common.shell.util.Preconditions;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * {@code ShellCommand} encapsulates a recognized command method annotated with {@link Command}.
 *
 * @author Martin Absmeier
 */
public class ShellCommand {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final String shortcut;
    @Getter
    private final Method method;
    @Getter
    private final ShellCommandParameter[] parameters;

    /**
     * Creates a new instance of {@link ShellCommand} class.
     *
     * @param name        the name of the command
     * @param description the description of the command
     * @param shortcut    the shortcut of the command
     * @param method      the method to call
     * @param parameters  the parameters of the method to call
     */
    @Builder
    public ShellCommand(String name, String description, String shortcut, Method method, ShellCommandParameter[] parameters) {
        this.name = name;
        this.description = description;
        this.shortcut = shortcut;
        this.method = method;
        this.parameters = parameters;
    }

    /**
     * Returns the entered parameters in the form in which they can be passed to the method.
     *
     * @return the parameters entered, typed for the method call
     */
    public Object[] getTypedParameters() {
        Class<?>[] paramTypes = method.getParameterTypes();

        if (paramTypes.length != parameters.length) {
            String message = "Number of parameters does not match, the method expects " + paramTypes.length
                + " parameters. Run help to show method signature.";
            throw new IllegalArgumentException(message);
        }

        Object[] typedParameters = new Object[paramTypes.length];
        int index = 0;
        for (ShellCommandParameter param : parameters) {
            typedParameters[index] = convertValueToType(param.getValue(), paramTypes[index]);
            index++;
        }

        return typedParameters;
    }

    // #################################################################################################################
    private Object convertValueToType(String value, Class<?> clazz) {
        if (Preconditions.isNullOrEmpty(value)) {
            String message = "[NULL] or empty string is not permitted for 'value' parameter.";
            throw new IllegalArgumentException(message);
        }

        if (clazz.equals(String.class) || clazz.isInstance(value)) {
            return value;
        } else if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
            return Integer.parseInt(value);
        } else if (clazz.equals(Long.class) || clazz.equals(Long.TYPE)) {
            return Long.parseLong(value);
        } else if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
            return Double.parseDouble(value);
        } else if (clazz.equals(Float.class) || clazz.equals(Float.TYPE)) {
            return Float.parseFloat(value);
        } else if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
            return Boolean.parseBoolean(value);
        } else {
            throw new ShellException("Can not convert value '" + value + "' to type '" + clazz + "'.");
        }
    }
}