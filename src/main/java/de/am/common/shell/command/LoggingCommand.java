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

import de.am.common.shell.Shell;
import de.am.common.shell.ShellInject;
import de.am.common.shell.command.annotation.Command;
import de.am.common.shell.command.annotation.CommandParameter;
import de.am.common.shell.util.Preconditions;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

/**
 * {@code EnableLoggingCommand} enables the logging of the shell.
 *
 * @author Martin Absmeier
 */
@NoArgsConstructor
public class LoggingCommand implements ShellInject {
    @Setter
    private Shell shell;

    /**
     * Enables the logging of the shell.
     *
     * @param fileName the name of the log file
     */
    @Command(name = "enableLogging", shortcut = "eL", description = "Enable the logging of the shell. Parameter is the file name e.g. myLogFile.log.")
    public void enableLogging(@CommandParameter(name = "file-name", description = "The name of the log file.") String fileName) {
        Preconditions.checkNotNull(fileName, "fileName");
        Path normalizedPath = Path.of(fileName.trim()).normalize();
        if (normalizedPath.isAbsolute() || normalizedPath.getNameCount() != 1) {
            throw new IllegalArgumentException("The file name for logging must not contain path elements.");
        }
        shell.enableLogging(fileName);
    }

    /**
     * Disables the logging of the shell.
     */
    @Command(name = "disableLogging", shortcut = "dL", description = "Disable the logging of the shell.")
    public void disableLogging() {
        shell.disableLogging();
    }
}