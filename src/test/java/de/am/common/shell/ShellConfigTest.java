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

import org.junit.jupiter.api.Test;

import static de.am.common.shell.ShellConstants.DEFAULT_APP_NAME;
import static de.am.common.shell.ShellConstants.DEFAULT_DISPLAY_TIME;
import static de.am.common.shell.ShellConstants.DEFAULT_PROMPT;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit test cases of {@link ShellConfig} class.
 *
 * @author Martin Absmeier
 */
class ShellConfigTest {

    @Test
    void constructorDefault() {
        ShellConfig config = ShellConfig.builder().build();

        assertEquals(DEFAULT_PROMPT, config.getPrompt());
        assertEquals(DEFAULT_APP_NAME, config.getAppName());
        assertEquals(DEFAULT_DISPLAY_TIME, config.isTimeDisplayed());
    }

    @Test
    void constructorValues() {
        String prompt = "->";
        String appName = "TestApp";
        boolean isEnabled = true;

        ShellConfig config = ShellConfig.builder().prompt(prompt).appName(appName).isTimeDisplayed(isEnabled).build();

        assertEquals(prompt, config.getPrompt());
        assertEquals(appName, config.getAppName());
        assertEquals(isEnabled, config.isTimeDisplayed());
    }
}