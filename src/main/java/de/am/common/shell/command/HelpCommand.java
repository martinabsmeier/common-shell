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

import java.util.List;

import static de.am.common.shell.ShellConstants.ANSI_WHITE_BRIGHT;
import static java.lang.Math.max;
import static java.util.Objects.isNull;

/**
 * {@code HelpCommand} list all available command known by the shell.
 *
 * @author Martin Absmeier
 */
@NoArgsConstructor
public class HelpCommand implements ShellInject {
    @Setter
    private Shell shell;

    /**
     * List all commands known by the shell.
     */
    @Command(name = "help", shortcut = "help", description = "List of all commands.")
    public void help() {
        int[] maxLength = calculateLength();
        printHeader(maxLength);

        OutputProvider out = shell.getOutputProvider();

        List<ShellCommand> commands = shell.getDictionary().getCommands();
        commands.forEach(command -> {
            String cmd = addSpace(command.getName(), maxLength[0]);
            String shortCut = addSpace(command.getShortcut(), maxLength[1]);
            String parameters = formatParameters(command.getParameters());
            parameters = addSpace(parameters, maxLength[2]);
            String description = isNullOrEmpty(command.getDescription()) ? "No description" : command.getDescription();
            description = addSpace(description, maxLength[3]);

            out.println("{0} | {1} | {2} | {3}", ANSI_WHITE_BRIGHT, cmd, shortCut, parameters, description);
        });
    }

    // #################################################################################################################
    private int[] calculateLength() {
        int[] result = new int[]{0, 8, 0, 0};

        List<ShellCommand> commands = shell.getDictionary().getCommands();
        for (ShellCommand command : commands) {
            result[0] = max(result[0], command.getName().length());
            result[1] = max(result[1], command.getShortcut().length());
            String parameters = formatParameters(command.getParameters());
            result[2] = max(result[2], parameters.length());
            String description = isNullOrEmpty(command.getDescription()) ? "No description" : command.getDescription();
            result[3] = max(result[3], description.length());
        }

        return result;
    }

    private void printHeader(int[] maxLength) {
        String command = addSpace("Command", maxLength[0]);
        String shortCut = addSpace("Shortcut", maxLength[1]);
        String parameter = addSpace("Parameter", maxLength[2]);
        String description = addSpace("Description", maxLength[3]);

        OutputProvider out = shell.getOutputProvider();
        out.println("{0} | {1} | {2} | {3}", ANSI_WHITE_BRIGHT, command, shortCut, parameter, description);

        String lineDelim = createDelim(maxLength[0]).concat("|")
            .concat(createDelim(maxLength[1])).concat("-|")
            .concat(createDelim(maxLength[2])).concat("-|")
            .concat(createDelim(maxLength[3]));
        out.println("{0}", ANSI_WHITE_BRIGHT, lineDelim);
    }

    private String createDelim(int nTimes) {
        String result = "";
        for (int i = 0; i <= nTimes; i++) {
            result = result.concat("-");
        }
        return result;
    }

    private String formatParameters(ShellCommandParameter[] parameters) {
        if (parameters.length == 0) {
            return "(no parameter)";
        }

        String result = "(";
        for (ShellCommandParameter param : parameters) {
            String paramType = param.getType().getTypeName();
            paramType = paramType.substring(paramType.lastIndexOf('.') + 1);
            String paramName = param.getName();
            result = result.concat(paramType).concat(" ").concat(paramName).concat(", ");
        }

        return result.substring(0, result.length() - 2).concat(")");
    }

    private boolean isNullOrEmpty(String value) {
        return isNull(value) || value.isEmpty();
    }

    private String addSpace(String value, int nTimes) {
        while (value.length() < nTimes) {
            value = value.concat(" ");
        }
        return value;
    }
}