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

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * {@code ShellCommandDictionary} is responsible for managing a lot of {@link ShellCommand} and its main function is to
 * return a command by name.
 *
 * @author Martin Absmeier
 */
public class ShellCommandDictionary {

    @Getter
    private final List<ShellCommand> commands;

    /**
     * Create a new instance of {@code ShellCommandDictionary}.
     *
     * @param commands the known commands
     */
    @Builder
    public ShellCommandDictionary(List<ShellCommand> commands) {
        this.commands = isNull(commands) ? new ArrayList<>() : commands;
    }

    /**
     * Maps the entered command to a {@link ShellCommand} if one is found, returns null otherwise
     *
     * @param command the command entered by the user
     * @return the found {@code ShellCommand} or null if no one is found
     */
    public ShellCommand getCommand(String command) {
        String[] commandAndParamValues = command.split(" ");

        ShellCommand cmd = findCommand(commandAndParamValues[0]);
        if (nonNull(cmd) && nonNull(cmd.getParameters())) {
            setParameterValues(commandAndParamValues, cmd.getParameters());
        }

        return cmd;
    }

    // #################################################################################################################
    private void setParameterValues(String[] paramValues, ShellCommandParameter[] parameters) {
        // the first value in paramValues is the command
        int valueLength = paramValues.length - 1;
        if (valueLength != parameters.length) {
            String message = "Number of parameters does not match, the method expects " + parameters.length
                + " parameters. Run help to show method signature.";
            throw new IllegalArgumentException(message);
        }

        int index = 1; // The parameters start at index 1 the command is at index 0
        for (ShellCommandParameter param : parameters) {
            param.setValue(paramValues[index]);
            index++;
        }
    }

    private ShellCommand findCommand(String command) {
        return commands.stream()
            .filter(cmd -> isNameOrShortCutEqual(cmd, command))
            .findFirst().orElse(null);
    }

    private boolean isNameOrShortCutEqual(ShellCommand cmd, String command) {
        return command.equals(cmd.getName()) || command.equals(cmd.getShortcut());
    }
}