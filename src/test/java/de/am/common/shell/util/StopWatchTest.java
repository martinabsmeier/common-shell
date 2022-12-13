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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit test cases of {@link StopWatch} class.
 *
 * @author Martin Absmeier
 */
class StopWatchTest {

    private StopWatch sw;

    @BeforeEach
    void setUp() {
        sw = StopWatch.builder().id("Unit test").build();
    }

    @Test
    void create() {
        assertNotNull(sw, "We expect a StopWatch instance.");
    }

    @Test
    void startIllegalStatException() {
        sw.start();

        assertThrows(IllegalStateException.class, sw::start, "We expect a thrown IllegalStateException.");
    }

    @Test
    void start() {
        sw.start();
        assertTrue(sw.isRunning(), "We expect a running stop watch.");

        await().atLeast(1, SECONDS);
        await().atLeast(1, SECONDS);

        sw.stop();
        assertFalse(sw.isRunning(), "We expect a not running stop watch.");

        System.out.println(sw.toString());
    }

    @Test
    void stopIllegalStatException() {
        sw.start();
        sw.stop();

        assertThrows(IllegalStateException.class, sw::stop, "We expect a thrown IllegalStateException.");
    }
}