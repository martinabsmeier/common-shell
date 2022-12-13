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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static de.am.common.shell.ShellConstants.ANSI_WHITE_BRIGHT;
import static de.am.common.shell.ShellConstants.APPLICATION_PROPERTIES_FILE_NAME;
import static de.am.common.shell.ShellConstants.JAVA_VENDOR;
import static de.am.common.shell.ShellConstants.JAVA_VERSION;
import static de.am.common.shell.ShellConstants.OS_ARCH;
import static de.am.common.shell.ShellConstants.OS_NAME;
import static de.am.common.shell.ShellConstants.SHELL_VERSION;

/**
 * {@code VersionCommand} show the version of the shell instance.
 *
 * @author Martin Absmeier
 */
@NoArgsConstructor
public class VersionCommand implements ShellInject {
    @Setter
    private Shell shell;

    /**
     * Print the information of shell artefact.
     */
    @Command(name = "version", shortcut = "version", description = "Show the version of the Shell.")
    public void info() {
        OutputProvider out = shell.getOutputProvider();

        try {
            Properties properties = loadProperties();
            String shellVersion = properties.getProperty(SHELL_VERSION);
            out.println("Shell [{0}] version runs on [{1} - {2}] under java [{3} - {4}]", ANSI_WHITE_BRIGHT, shellVersion, OS_NAME, OS_ARCH, JAVA_VERSION, JAVA_VENDOR);
        } catch (IOException ex) {
            shell.setException(ex);
            out.println("{0}", ANSI_WHITE_BRIGHT, "Exception occurred, run 'showException' or 'sE' to see the details.");
        }
    }

    // #################################################################################################################
    private Properties loadProperties() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream propertiesStream = classLoader.getResourceAsStream(APPLICATION_PROPERTIES_FILE_NAME)) {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties;
        }
    }
}