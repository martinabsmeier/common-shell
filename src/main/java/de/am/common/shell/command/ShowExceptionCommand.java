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
import de.am.common.shell.io.OutputProvider;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import static de.am.common.shell.ShellConstants.ANSI_WHITE_BRIGHT;

/**
 * {@code ShowExceptionCommand} show the last exception occurred during shell execution.
 *
 * @author Martin Absmeier
 */
@NoArgsConstructor
public class ShowExceptionCommand implements ShellInject {
    @Setter
    private Shell shell;

    /**
     * Print out the last exception.
     */
    @Command(name = "showException", shortcut = "sE", description = "Show the last exception occurred.")
    public void showException() {
        OutputProvider out = shell.getOutputProvider();

        Exception exception = shell.getException();
        if (exception == null) {
            out.println("{0}", ANSI_WHITE_BRIGHT, "No exception occurred.");
        } else {
            String message = Objects.isNull(exception.getMessage()) ? "No message" : exception.getMessage();
            out.println("Message   : {0}", ANSI_WHITE_BRIGHT, message);
            StackTraceElement[] stackTrace = exception.getStackTrace();
            for (StackTraceElement element : stackTrace) {
                out.println("{0}", ANSI_WHITE_BRIGHT, element.toString());
            }
        }
    }
}