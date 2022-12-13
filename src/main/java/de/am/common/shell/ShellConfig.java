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

import de.am.common.shell.io.DefaultInputProvider;
import de.am.common.shell.io.DefaultOutputProvider;
import de.am.common.shell.io.InputProvider;
import de.am.common.shell.io.OutputProvider;
import lombok.Builder;
import lombok.Getter;

import static de.am.common.shell.ShellConstants.DEFAULT_APP_NAME;
import static de.am.common.shell.ShellConstants.DEFAULT_DISPLAY_TIME;
import static de.am.common.shell.ShellConstants.DEFAULT_PROMPT;
import static de.am.common.shell.util.Preconditions.isNullOrEmpty;
import static java.util.Objects.isNull;

/**
 * {@code ShellConfig} contains the configuration the {@link Shell} application.
 *
 * @author Martin Absmeier
 */
public class ShellConfig {
    @Getter
    private final String prompt;
    @Getter
    private final String appName;
    @Getter
    private final InputProvider inputProvider;
    @Getter
    private final OutputProvider outputProvider;
    @Getter
    private final boolean isTimeDisplayed;

    /**
     * Create a new instance of {@code ShellConfig}.
     *
     * @param prompt          the prompt of the shell
     * @param appName         the name of the application using shell
     * @param inputProvider   the input provider of shell
     * @param outputProvider  the output provider of shell
     * @param isTimeDisplayed true if the runtime of commands should be measured, false otherwise
     */
    @Builder
    public ShellConfig(String prompt,
                       String appName,
                       InputProvider inputProvider,
                       OutputProvider outputProvider,
                       Boolean isTimeDisplayed)
    {
        this.prompt = isNullOrEmpty(prompt) ? DEFAULT_PROMPT : prompt;
        this.appName = isNullOrEmpty(appName) ? DEFAULT_APP_NAME : appName;
        this.inputProvider = isNull(inputProvider) ? DefaultInputProvider.builder().build() : inputProvider;
        this.outputProvider = isNull(outputProvider) ? DefaultOutputProvider.builder().build() : outputProvider;
        this.isTimeDisplayed = isNull(isTimeDisplayed) ? DEFAULT_DISPLAY_TIME : isTimeDisplayed;
    }
}