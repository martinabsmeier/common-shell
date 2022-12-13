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
package de.am.common.shell;

import de.am.common.shell.command.DisplayTimeCommand;
import de.am.common.shell.command.ExitCommand;
import de.am.common.shell.command.HelpCommand;
import de.am.common.shell.command.LoggingCommand;
import de.am.common.shell.command.ShellCommand;
import de.am.common.shell.command.ShellCommandDictionary;
import de.am.common.shell.command.ShellCommandParameter;
import de.am.common.shell.command.ShowExceptionCommand;
import de.am.common.shell.command.VersionCommand;
import de.am.common.shell.command.annotation.Command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.am.common.shell.ShellConstants.SPLIT_REGEX;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;

/**
 * {@code ShellFactory} is responsible for creating a {@link Shell} instance.
 *
 * @author Martin Absmeier
 */
public final class ShellFactory {

    /**
     * Create a new {@link Shell} instance with the specified parameters.
     *
     * @param prompt          the prompt of the shell if no prompt is specified the {@link ShellConstants#DEFAULT_PROMPT}
     *                        is used
     * @param appName         the name of the application if no name is specified the {@link ShellConstants#DEFAULT_APP_NAME}
     *                        is used
     * @param commandHandlers contain the commands that can be executed by the shell
     * @return a shell instance
     */
    public static Shell createShell(String prompt, String appName, Object... commandHandlers) {
        ShellConfig config = ShellConfig.builder().prompt(prompt).appName(appName).build();
        return createShell(config, commandHandlers);
    }

    /**
     * Create a new {@link Shell} instance with the specified parameters.
     *
     * @param config          the configuration of the shell see {@link ShellConfig}
     * @param commandHandlers contain the commands that can be executed by the shell
     * @return a shell instance
     */
    public static Shell createShell(ShellConfig config, Object... commandHandlers) {
        List<Object> commandList = addStandardCommands(commandHandlers);
        List<ShellCommand> commands = commandList.stream()
            .map(ShellFactory::createCommands)
            .flatMap(List::stream)
            .collect(Collectors.toList());
        ShellCommandDictionary dictionary = ShellCommandDictionary.builder().commands(commands).build();

        return Shell.builder().config(config).dictionary(dictionary).build();
    }

    // #################################################################################################################
    private static List<ShellCommand> createCommands(Object commandHandler) {
        List<ShellCommand> commands = new ArrayList<>();

        for (Method method : commandHandler.getClass().getMethods()) {
            Command cmdAnnotation = method.getAnnotation(Command.class);
            if (nonNull(cmdAnnotation)) {
                String name = cmdAnnotation.name().isEmpty() ? method.getName() : cmdAnnotation.name();
                String shortCut = cmdAnnotation.shortcut().isEmpty() ? createShortCut(method.getName()) : cmdAnnotation.shortcut();

                if (!containsCommand(name, shortCut, commands)) {
                    String description = cmdAnnotation.description();
                    commands.add(
                        ShellCommand.builder()
                            .method(method).name(name).shortcut(shortCut).description(description)
                            .parameters(createParameters(method))
                            .build()
                    );
                }
            }
        }

        return commands;
    }

    private static boolean containsCommand(String name, String shortCut, List<ShellCommand> commands) {
        ShellCommand command = commands.stream()
            .filter(cmd -> cmd.getName().equals(name) || cmd.getShortcut().equals(shortCut))
            .findFirst().orElse(null);
        return command != null;
    }

    private static ShellCommandParameter[] createParameters(Method method) {
        Class<?>[] methodParamTypes = method.getParameterTypes();
        ShellCommandParameter[] commandParameters = new ShellCommandParameter[methodParamTypes.length];

        if (methodParamTypes.length > 0) {
            int index = 0;
            for (Class<?> methodParamType : methodParamTypes) {
                String name = "param" + index;
                commandParameters[index] = ShellCommandParameter.builder().index(index).name(name).type(methodParamType).build();
                index++;
            }
        }

        return commandParameters;
    }

    private static List<Object> addStandardCommands(Object... commandHandlers) {
        List<Object> commandHandlerList = new ArrayList<>();

        // Add standard commands
        commandHandlerList.add(new LoggingCommand());
        commandHandlerList.add(new ExitCommand());
        commandHandlerList.add(new HelpCommand());
        commandHandlerList.add(new ShowExceptionCommand());
        commandHandlerList.add(new VersionCommand());
        commandHandlerList.add(new DisplayTimeCommand());

        // Add user commands
        if (nonNull(commandHandlers) && commandHandlers.length > 0) {
            commandHandlerList.addAll(Arrays.asList(commandHandlers));
        }

        return commandHandlerList;
    }

    private static String createShortCut(String methodName) {
        return stream(methodName.split(SPLIT_REGEX))
            .map(word -> word.substring(0, 1))
            .collect(Collectors.joining());
    }

    private ShellFactory() {
        // No instance needed
    }
}