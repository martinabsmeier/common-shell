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
import de.am.common.shell.io.InputProvider;
import de.am.common.shell.io.OutputProvider;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static de.am.common.shell.ShellConstants.ANSI_WHITE_BRIGHT;

/**
 * {@code ExitCommand} shut down current running shell instance.
 *
 * @author Martin Absmeier
 */
@NoArgsConstructor
public class ExitCommand implements ShellInject {
    @Setter
    private Shell shell;

    /**
     * Shutdown the shell.
     */
    @Command(name = "exit", shortcut = "exit", description = "Exit the shell.")
    public void exit() {
        shell.setShutdown(true);

        InputProvider input = shell.getInputProvider();
        input.exit();

        OutputProvider out = shell.getOutputProvider();
        out.println("{0}", ANSI_WHITE_BRIGHT, "Shutdown shell...");
        out.exit();
    }
}