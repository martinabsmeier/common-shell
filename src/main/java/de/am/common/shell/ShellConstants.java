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

/**
 * {@code ShellConstants} contains all constants of the {@link Shell} application.
 *
 * @author Martin Absmeier
 */
public final class ShellConstants {

    // #################################################################################################################
    // Default values
    /**
     * If no prompt is specified this default one is used
     */
    public static final String DEFAULT_PROMPT = "> ";
    /**
     * If no app name is specified this default one is used
     */
    public static final String DEFAULT_APP_NAME = "Shell";
    /**
     * The execution time of commands is not measured by default
     */
    public static final boolean DEFAULT_DISPLAY_TIME = false;

    // #################################################################################################################
    // Common constants
    /**
     * Code for successful termination of the JVM
     */
    public static final int SUCCESSFUL = 0;
    /**
     * System property of OS dependant line separator
     */
    public static final String NEW_LINE = System.getProperty("line.separator");
    /**
     * System property of java version
     */
    public static final String JAVA_VERSION = System.getProperty("java.version");
    /**
     * System property of java vendor
     */
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");
    /**
     * System property of OS architecture
     */
    public static final String OS_ARCH = System.getProperty("os.arch");
    /**
     * System property of OS name
     */
    public static final String OS_NAME = System.getProperty("os.name");
    /**
     * File name of the application property config file
     */
    public static final String APPLICATION_PROPERTIES_FILE_NAME = "application.properties";
    /**
     * File name of the application property config file
     */
    public static final String SHELL_VERSION = "project.version";
    /**
     * RegEx uses to create a shortcut name of a command
     */
    public static final String SPLIT_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

    // #################################################################################################################
    // Logging configuration
    /**
     * System property key of java logging config file
     */
    public static final String LOGGING_PROPERTIES_KEY = "java.util.logging.config.file";
    /**
     * File name of java logging properties file
     */
    public static final String LOGGING_PROPERTIES_FILE_NAME = "logging.properties";

    // #################################################################################################################
    // ANSI colors
    /**
     * Reset the ansi colour
     */
    public static final String ANSI_RESET = "\u001B[0m";
    /**
     * ANSI colour bright yellow
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[93m";
    /**
     * ANSI colour bright white
     */
    public static final String ANSI_WHITE_BRIGHT = "\u001B[97m";
    // #################################################################################################################
    // Messages
    /**
     * Preconditions message when checking NULL values permitted
     */
    public static final String NULL_IS_NOT_PERMITTED = "[NULL] is not permitted as value for [{0}] parameter.";

    // #################################################################################################################
    private ShellConstants() {
        // we do not need an instance
    }
}