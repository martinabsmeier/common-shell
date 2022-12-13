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
package de.am.common.shell.util;

import de.am.common.shell.ShellConstants;

import static de.am.common.shell.ShellConstants.NULL_IS_NOT_PERMITTED;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * {@code Preconditions} provides static convenience methods that help a method or constructor check whether it was invoked
 * correctly (whether its preconditions have been met).
 *
 * @author Martin Absmeier
 */
public final class Preconditions {

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference   an object reference
     * @param nameOfParam is used to replace the placeholder in the message {@link ShellConstants#NULL_IS_NOT_PERMITTED}
     * @param <T>         the type of the reference
     * @throws NullPointerException if the reference is null
     */
    public static <T> void checkNotNull(T reference, String nameOfParam) {
        requireNonNull(reference, format(NULL_IS_NOT_PERMITTED, nameOfParam));
    }

    /**
     * Check if the string specified by {@code value} is NULL or empty.
     *
     * @param value the string to be checked
     * @return true if value is NULL or empty string false otherwise
     */
    public static boolean isNullOrEmpty(String value) {
        return isNull(value) || value.isEmpty();
    }

    // #################################################################################################################
    private Preconditions() {
        // No instance needed
    }
}