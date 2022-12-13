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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit test cases of {@link ShellCommandDictionary} class.
 *
 * @author Martin Absmeier
 */
class ShellCommandDictionaryTest {

    private ShellCommandDictionary dictionary;

    @Test
    void builder() {
        dictionary = ShellCommandDictionary.builder().build();
        assertNotNull(dictionary, "Instance expected.");
        assertNotNull(dictionary.getCommands(), "Command list expected.");
    }

    @Test
    void builderNull() {
        dictionary = ShellCommandDictionary.builder().commands(null).build();
        assertNotNull(dictionary, "Instance expected.");

        List<ShellCommand> commands = dictionary.getCommands();
        assertNotNull(commands, "Command list expected.");
        assertTrue(commands.isEmpty(), "Shell command list not empty.");
    }

    @Test
    void commandsList() {
        dictionary = ShellCommandDictionary.builder().commands(createCommands()).build();

        List<ShellCommand> actual = dictionary.getCommands();
        assertEquals(3, actual.size(), "We expect three commands.");
    }

    @Test
    void getCommandByName() {
        dictionary = ShellCommandDictionary.builder().commands(createCommands()).build();

        String expectedName = "cmd-3";
        ShellCommand actual = dictionary.getCommand(expectedName);
        assertNotNull(actual, "We expect one command");
        assertEquals(expectedName, actual.getName(), "Command name not equal.");
    }

    @Test
    void getCommandByShortCut() {
        dictionary = ShellCommandDictionary.builder().commands(createCommands()).build();

        String expectedShortCut = "c3";
        ShellCommand actual = dictionary.getCommand(expectedShortCut);
        assertNotNull(actual, "We expect one command");
        assertEquals(expectedShortCut, actual.getShortcut(), "Command short cut not equal.");
    }

    @Test
    void getCommandIllegalArgumentException() {
        ShellCommandParameter[] parameters = new ShellCommandParameter[]{
            ShellCommandParameter.builder().build(),
            ShellCommandParameter.builder().build(),
            ShellCommandParameter.builder().build()
        };
        List<ShellCommand> commands = new ArrayList<>();
        commands.add(ShellCommand.builder().name("cmd").shortcut("cmd").parameters(parameters).build());
        dictionary = ShellCommandDictionary.builder().commands(commands).build();

        assertThrows(IllegalArgumentException.class, () -> dictionary.getCommand("cmd"));
    }

    @Test
    void getCommand() {
        ShellCommandParameter[] parameters = new ShellCommandParameter[]{
            ShellCommandParameter.builder().name("param-1").build(),
            ShellCommandParameter.builder().name("param-2").build()
        };
        List<ShellCommand> commands = new ArrayList<>();
        commands.add(ShellCommand.builder().name("cmd").shortcut("cmd").parameters(parameters).build());
        dictionary = ShellCommandDictionary.builder().commands(commands).build();

        ShellCommand command = dictionary.getCommand("cmd value-1 value-2");
        assertNotNull(command, "We expect a command.");
        assertEquals(2, command.getParameters().length, "We expect two parameters.");
    }

    // #################################################################################################################
    private List<ShellCommand> createCommands() {
        List<ShellCommand> commands = new ArrayList<>();

        commands.add(createCommand("cmd-1", "c1"));
        commands.add(createCommand("cmd-2", "c2"));
        commands.add(createCommand("cmd-3", "c3"));

        return commands;
    }

    private ShellCommand createCommand(String name, String shortCut) {
        return ShellCommand.builder().name(name).shortcut(shortCut).build();
    }
}