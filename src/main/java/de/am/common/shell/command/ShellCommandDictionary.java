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
        String[] commandAndParamValues = tokenize(command);
        if (commandAndParamValues.length == 0) {
            return null;
        }

        ShellCommand cmd = findCommand(commandAndParamValues[0]);
        if (nonNull(cmd) && nonNull(cmd.getParameters())) {
            return createCommandWithParameterValues(commandAndParamValues, cmd);
        }

        return cmd;
    }

    // #################################################################################################################
    private ShellCommand createCommandWithParameterValues(String[] paramValues, ShellCommand command) {
        ShellCommandParameter[] parameters = command.getParameters();
        // the first value in paramValues is the command
        int valueLength = paramValues.length - 1;
        if (valueLength != parameters.length) {
            String message = "Number of parameters does not match, the method expects " + parameters.length
                + " parameters. Run help to show method signature.";
            throw new IllegalArgumentException(message);
        }

        ShellCommandParameter[] parsedParameters = new ShellCommandParameter[parameters.length];
        int index = 1; // The parameters start at index 1 the command is at index 0
        for (int paramIndex = 0; paramIndex < parameters.length; paramIndex++) {
            ShellCommandParameter param = parameters[paramIndex];
            parsedParameters[paramIndex] = param.withValue(paramValues[index]);
            index++;
        }

        return command.withParameters(parsedParameters);
    }

    private ShellCommand findCommand(String command) {
        return commands.stream()
            .filter(cmd -> isNameOrShortCutEqual(cmd, command))
            .findFirst().orElse(null);
    }

    private boolean isNameOrShortCutEqual(ShellCommand cmd, String command) {
        return command.equals(cmd.getName()) || command.equals(cmd.getShortcut());
    }

    private String[] tokenize(String command) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean tokenStarted = false;
        char activeQuote = 0;

        for (int index = 0; index < command.length(); index++) {
            char currentChar = command.charAt(index);

            if (activeQuote == 0 && (currentChar == '"' || currentChar == '\'')) {
                activeQuote = currentChar;
                tokenStarted = true;
            } else if (activeQuote != 0 && currentChar == activeQuote) {
                activeQuote = 0;
            } else if (activeQuote == 0 && Character.isWhitespace(currentChar)) {
                if (tokenStarted) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                    tokenStarted = false;
                }
            } else {
                currentToken.append(currentChar);
                tokenStarted = true;
            }
        }

        if (activeQuote != 0) {
            throw new IllegalArgumentException("Command contains an unterminated quoted parameter.");
        }

        if (tokenStarted) {
            tokens.add(currentToken.toString());
        }

        return tokens.toArray(new String[0]);
    }
}