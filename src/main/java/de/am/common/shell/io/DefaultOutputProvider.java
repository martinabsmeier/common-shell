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

import de.am.common.shell.exception.ShellException;
import de.am.common.shell.util.Preconditions;
import lombok.Builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static de.am.common.shell.ShellConstants.ANSI_RESET;
import static de.am.common.shell.ShellConstants.LOGGING_PROPERTIES_FILE_NAME;
import static de.am.common.shell.ShellConstants.LOGGING_PROPERTIES_KEY;
import static de.am.common.shell.ShellConstants.NEW_LINE;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.INFO;

/**
 * {@code DefaultOutputProvider} is the standard {@link OutputProvider} and write all to the console.
 *
 * @author Martin Absmeier
 */
public class DefaultOutputProvider implements OutputProvider {

    private final Logger logger;
    private boolean isLoggingEnabled;
    private Path logFilePath;

    /**
     * Creates a new instance of {@link DefaultOutputProvider} class.<br>
     * <b>Note:</b> The builder is only given a logger during a JUnit test
     *
     * @param logger a logger should only be passed for a JUnit test
     */
    @Builder
    public DefaultOutputProvider(Logger logger) {
        // Read the configuration
        ClassLoader classLoader = getClass().getClassLoader();
        String loggingProperties = requireNonNull(classLoader.getResource(LOGGING_PROPERTIES_FILE_NAME)).getFile();
        System.setProperty(LOGGING_PROPERTIES_KEY, loggingProperties);

        this.logger = isNull(logger) ? Logger.getLogger("ConsoleLogger") : logger;
        this.isLoggingEnabled = false;
        this.logFilePath = null;
    }

    @Override
    public void print(String pattern, String ansiColour, Object... arguments) {
        Preconditions.checkNotNull(pattern, "pattern");
        pattern = isNull(ansiColour) ? pattern : ansiColour.concat(pattern).concat(ANSI_RESET);

        if (logger.isLoggable(INFO)) {
            logger.log(INFO, pattern, arguments);
        }

        if (isLoggingEnabled) {
            writeString(pattern, arguments);
            writeString("{0}", NEW_LINE);
        }
    }

    @Override
    public void println(String pattern, String ansiColour, Object... arguments) {
        print(pattern, ansiColour, arguments);

        if (logger.isLoggable(INFO)) {
            logger.log(INFO, NEW_LINE);
        }
    }

    @Override
    public void exit() {
        // No resources to clean up
    }

    @Override
    public void enableLogging(String fileName) {
        isLoggingEnabled = true;
        initLogFile(fileName);
    }

    @Override
    public void disableLogging() {
        isLoggingEnabled = false;
    }

    // #################################################################################################################
    private void initLogFile(String fileName) {
        // Add .log if necessary
        if (!fileName.endsWith(".log")) {
            fileName = fileName.concat(".log");
        }

        logFilePath = Paths.get(fileName);
    }

    private void writeString(String pattern, Object... arguments) {
        try {
            Files.writeString(logFilePath, format(pattern, arguments), CREATE, APPEND);
        } catch (IOException ex) {
            ShellException shellException = new ShellException("Can not write to log file.");
            shellException.initCause(ex);
            throw shellException;
        }
    }
}