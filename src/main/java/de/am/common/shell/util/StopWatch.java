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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Simple {@code StopWatch} to measure the running time of commands.
 *
 * @author Martin Absmeier
 */
public class StopWatch {

    /**
     * Identifier of the {@link StopWatch}.
     */
    @Getter
    @Setter
    private String id;

    /**
     * Start time of the stop watch
     */
    @Getter
    private long startTimeNanos;

    /**
     * Running time of the stop watch.
     */
    @Getter
    private long duration;

    /**
     * Switch that indicates whether the stop watch is running.
     */
    @Getter
    private boolean isRunning;

    /**
     * Creates a new instance of {@link StopWatch} with the specified id.
     *
     * @param id the identifier of the stop watch.
     */
    @Builder
    public StopWatch(String id) {
        this.id = id;
    }

    /**
     * Start the stop watch.
     *
     * @throws IllegalStateException if the stop watch is already running
     * @see #stop()
     */
    public void start() {
        if (isRunning()) {
            throw new IllegalStateException("Can not start StopWatch it is already running.");
        }
        isRunning = true;
        startTimeNanos = System.nanoTime();
    }

    /**
     * Stop the stop watch.
     *
     * @throws IllegalStateException if the stop watch is not running
     * @see #start()
     */
    public void stop() {
        if (!isRunning()) {
            throw new IllegalStateException("Can not stop StopWatch it is not running.");
        }
        isRunning = false;
        duration = System.nanoTime() - getStartTimeNanos();
    }

    @Override
    public String toString() {
        return "Command [".concat(getId()).concat("] executed in = ").concat(nanosToMillis(getDuration())).concat(" ms");
    }

    // #################################################################################################################

    private String nanosToMillis(long nanoSeconds) {
        return Long.toString(NANOSECONDS.toMillis(nanoSeconds));
    }
}