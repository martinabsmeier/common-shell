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

import java.text.MessageFormat;

/**
 * {@code OutputProvider} is the interface off all output providers.<p>
 * A provider is only responsible for the output on the console, not for the output in a log file.
 *
 * @author Martin Absmeier
 */
public interface OutputProvider {

    /**
     * /**
     * Print a formatted string with the given pattern and uses it to format the given arguments.<p>
     * This is equivalent to {@link MessageFormat#format(String, Object...)}
     *
     * @param pattern    the pattern string arguments
     * @param ansiColour the colour in ANSI format
     * @param arguments  the arguments
     * @throws IllegalArgumentException if the pattern is invalid, or if an argument in the arguments array is not of the
     *                                  type expected by the format element(s) that use it.
     * @throws NullPointerException     if pattern is null
     */
    void print(String pattern, String ansiColour, Object... arguments);

    /**
     * Print a formatted string with the given pattern and uses it to format the given arguments.<p>
     * This is equivalent to {@link MessageFormat#format(String, Object...)}
     *
     * @param pattern    the pattern string arguments
     * @param ansiColour the colour in ANSI format
     * @param arguments  the arguments
     * @throws IllegalArgumentException if the pattern is invalid, or if an argument in the arguments array is not of the
     *                                  type expected by the format element(s) that use it.
     * @throws NullPointerException     if pattern is null
     */
    void println(String pattern, String ansiColour, Object... arguments);

    /**
     * Is called if the shell is terminated. Used to close resources of the output provider.
     */
    void exit();

    /**
     * Turns on writing to a log file, the file specified by {@code fileName} is created in "user.dir".
     *
     * @param fileName the name of the log file
     */
    void enableLogging(String fileName);

    /**
     * Turns off writing to a log file.
     */
    void disableLogging();
}