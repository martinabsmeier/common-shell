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

import de.am.common.shell.command.ShellCommand;
import de.am.common.shell.command.ShellCommandDictionary;
import de.am.common.shell.io.InputProvider;
import de.am.common.shell.io.OutputProvider;
import de.am.common.shell.util.StopWatch;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

import static de.am.common.shell.ShellConstants.ANSI_WHITE_BRIGHT;
import static de.am.common.shell.ShellConstants.ANSI_YELLOW_BRIGHT;
import static java.util.Objects.nonNull;

/**
 * {@code Shell} is the class interacting with user and provides the command loop.
 * All logic lies here.
 *
 * @author Martin Absmeier
 */
public class Shell {
    @Getter
    private final ShellCommandDictionary dictionary;
    @Getter
    private final InputProvider inputProvider;
    @Getter
    private final OutputProvider outputProvider;
    @Getter
    private final String prompt;
    @Getter
    @Setter
    private boolean isShutdown;
    @Getter
    @Setter
    private Exception exception;
    @Getter
    @Setter
    private boolean isTimeDisplayed;
    private final StopWatch sw;

    private static final String EX_MESSAGE = "Exception occurred, run 'showException' or 'sE' to see the details.";

    /**
     * Create a new instance of {@code Shell}.
     *
     * @param config     the configuration of the shell
     * @param dictionary the dictionary with the commands
     */
    @Builder
    public Shell(ShellConfig config, ShellCommandDictionary dictionary) {
        this.dictionary = dictionary;
        this.inputProvider = config.getInputProvider();
        this.outputProvider = config.getOutputProvider();
        this.prompt = config.getAppName().concat(config.getPrompt());
        this.isTimeDisplayed = config.isTimeDisplayed();
        this.sw = StopWatch.builder().build();
    }

    /**
     * Main loop of the shell.
     */
    public void execute() {
        outputProvider.print(prompt, ANSI_YELLOW_BRIGHT);

        String command;
        while (!isShutdown) {
            command = inputProvider.readCommand();

            sw.setId(command);
            sw.start();

            if (!command.isEmpty()) {
                executeCommand(command);
            }

            sw.stop();
            if (isTimeDisplayed()) {
                outputProvider.println(sw.toString(), ANSI_WHITE_BRIGHT);
            }

            outputProvider.print(prompt, ANSI_YELLOW_BRIGHT);
        }
    }

    /**
     * Enables the logging of shell component and writes to specified {@code fileName} logfile.
     *
     * @param fileName the name of the log file
     */
    public void enableLogging(String fileName) {
        outputProvider.enableLogging(fileName);
    }

    /**
     * Disables the logging of shell.
     */
    public void disableLogging() {
        outputProvider.disableLogging();
    }

    // #################################################################################################################
    private void executeCommand(String command) {
        try {
            ShellCommand cmd = dictionary.getCommand(command);
            if (nonNull(cmd)) {
                Method method = cmd.getMethod();
                Object result = executeMethod(method, cmd.getTypedParameters());
                if (nonNull(result)) {
                    outputProvider.println("{0}", ANSI_WHITE_BRIGHT, result);
                }
            } else {
                outputProvider.println("Command [{0}] not found.", ANSI_WHITE_BRIGHT, command);
            }
        } catch (Exception ex) {
            exception = ex;
            outputProvider.println("{0}", ANSI_WHITE_BRIGHT, EX_MESSAGE);
        }
    }

    private Object executeMethod(Method method, Object[] parameters) {
        try {
            Class<?> clazz = Class.forName(method.getDeclaringClass().getName());
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (ShellInject.class.isAssignableFrom(clazz)) {
                ((ShellInject) instance).setShell(this);
            }
            return method.invoke(instance, parameters);
        } catch (Exception ex) {
            exception = ex;
            outputProvider.println("{0}", ANSI_WHITE_BRIGHT, EX_MESSAGE);
            return null;
        }
    }
}