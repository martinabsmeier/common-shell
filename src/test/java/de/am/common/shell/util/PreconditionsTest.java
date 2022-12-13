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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit test cases of {@link Preconditions} class.
 *
 * @author Martin Absmeier
 */
class PreconditionsTest {

    @Test
    void checkNotNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> Preconditions.checkNotNull(null, "test"));
        assertEquals("[NULL] is not permitted as value for [test] parameter.", exception.getMessage());
    }

    @Test
    void isNullOrEmptyNull() {
        Assertions.assertTrue(Preconditions.isNullOrEmpty(null), "Value not NULL");
    }

    @Test
    void isNullOrEmptyEmpty() {
        Assertions.assertTrue(Preconditions.isNullOrEmpty(""), "Value not empty.");
    }
}