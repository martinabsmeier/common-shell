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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Martin Absmeier
 */
@NoArgsConstructor
public class DisplayTimeCommand implements ShellInject {
    @Setter
    private Shell shell;

    /**
     * Turns on the display of the runtime of a command.
     */
    @Command(name = "enableTimeDisplay", description = "Switches on the runtime display for commands.")
    public void enableDisplayTime() {
        shell.setTimeDisplayed(true);
    }

    /**
     * Turns off the display of the runtime of a command
     */
    @Command(name = "disableDisplayTime", description = "Switches off the runtime display for commands.")
    public void disableDisplayTime() {
        shell.setTimeDisplayed(false);
    }
}