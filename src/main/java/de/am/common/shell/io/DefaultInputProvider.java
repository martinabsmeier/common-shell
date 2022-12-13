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

import lombok.Builder;

import java.util.Scanner;

import static java.util.Objects.isNull;

/**
 * {@code DefaultInputProvider} is the standard {@link InputProvider}.
 *
 * @author Martin Absmeier
 */
public class DefaultInputProvider implements InputProvider {

    private final Scanner scanner;

    /**
     * Creates a new instance of {@link DefaultInputProvider} class.
     * <b>Note:</b> The builder is only given a {@link Scanner} during a JUnit test
     *
     * @param scanner a scanner should only be passed for a JUnit test
     */
    @Builder
    public DefaultInputProvider(Scanner scanner) {
        this.scanner = isNull(scanner) ? new Scanner(System.in) : scanner;
    }

    @Override
    public String readCommand() {
        return scanner.nextLine();
    }

    @Override
    public void exit() {
        scanner.close();
    }

}