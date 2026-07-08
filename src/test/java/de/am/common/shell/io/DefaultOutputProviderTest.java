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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.am.common.shell.ShellConstants.ANSI_WHITE_BRIGHT;
import static de.am.common.shell.ShellConstants.NEW_LINE;
import static java.util.logging.Level.INFO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit test cases of {@link DefaultOutputProvider} class.
 *
 * @author Martin Absmeier
 */
class DefaultOutputProviderTest {

    private Logger logger;
    private DefaultOutputProvider outputProvider;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        when(logger.isLoggable(INFO)).thenReturn(true);

        outputProvider = DefaultOutputProvider.builder().logger(logger).build();
    }

    @Test
    void printPatternNull() {
        assertThrows(NullPointerException.class, () -> outputProvider.print(null, null), "NullPointerException expected but not thrown.");
    }

    @Test
    void printAnsiColourNull() {
        outputProvider.print("{0}", null);

        verify(logger).log(any(Level.class), anyString(), aryEq(new Object[0]));
    }

    @Test
    void printAnsiColourNotNull() {
        outputProvider.print("{0}", ANSI_WHITE_BRIGHT);

        verify(logger).log(any(Level.class), anyString(), aryEq(new Object[0]));
    }

    @Test
    void println() {
        outputProvider.println("{0}", ANSI_WHITE_BRIGHT);

        verify(logger).log(any(Level.class), anyString(), aryEq(new Object[0]));
    }

    @Test
    void exit() {
        outputProvider.exit();
        assertTrue(true, "We expect no exception.");

        outputProvider = mock(DefaultOutputProvider.class);
        outputProvider.exit();

        verify(outputProvider).exit();
    }

    @Test
    void enableLoggingNoExtension() {
        outputProvider.enableLogging("logNoExtension");
        assertTrue(true, "We expect no exception.");

        outputProvider.enableLogging("logWithExtension.log");
        assertTrue(true, "We expect no exception.");
    }

    @Test
    void enableLoggingRejectsNestedPath() {
        String nestedFileName = "nested" + File.separator + "logfile";

        assertThrows(IllegalArgumentException.class, () -> outputProvider.enableLogging(nestedFileName));
    }

    @Test
    void enableLoggingRejectsAbsolutePath() {
        String absoluteFileName = Path.of(System.getProperty("user.home"), "logfile.log").toString();

        assertThrows(IllegalArgumentException.class, () -> outputProvider.enableLogging(absoluteFileName));
    }

    @Test
    void enableLoggingRejectsSymbolicLink() throws IOException {
        Path targetFile = Path.of("target-log-file.log");
        Path symbolicLink = Path.of("symbolic-link.log");

        try {
            Files.deleteIfExists(symbolicLink);
            Files.deleteIfExists(targetFile);
            Files.createFile(targetFile);
            Files.createSymbolicLink(symbolicLink, targetFile);

            assertThrows(IllegalArgumentException.class, () -> outputProvider.enableLogging(symbolicLink.getFileName().toString()));
        } finally {
            Files.deleteIfExists(symbolicLink);
            Files.deleteIfExists(targetFile);
        }
    }

    @Test
    void enableLogging() {
        String fileName = "logWithExtension.log";
        outputProvider.enableLogging(fileName);
        outputProvider.println("{0}", ANSI_WHITE_BRIGHT);

        verify(logger).log(any(Level.class), anyString(), aryEq(new Object[0]));

        String pathAndFileName = System.getProperty("user.dir").concat(File.separator).concat(fileName);
        File logFile = new File(pathAndFileName);
        assertTrue(logFile.delete(), "Can not delete log file.");
    }

    @Test
    void enableLoggingTruncatesLongLogEntry() throws IOException {
        outputProvider = DefaultOutputProvider.builder().logger(logger).maxLogEntryLength(5).build();
        String fileName = "truncated-" + System.nanoTime() + ".log";
        outputProvider.enableLogging(fileName);
        outputProvider.print("{0}", null, "123456789");

        String pathAndFileName = System.getProperty("user.dir") + File.separator + fileName;
        File logFile = new File(pathAndFileName);
        try {
            String content = Files.readString(logFile.toPath());
            assertTrue(("12345" + NEW_LINE).equals(content), "The log entry should be truncated to the configured maximum length.");
        } finally {
            assertTrue(logFile.delete(), "Can not delete log file.");
        }
    }

    @Test
    void constructorDoesNotChangeLoggingSystemProperty() {
        String propertyKey = "java.util.logging.config.file";
        String originalValue = System.getProperty(propertyKey);

        try {
            System.setProperty(propertyKey, "existing-config.properties");
            DefaultOutputProvider.builder().logger(logger).build();
            assertTrue("existing-config.properties".equals(System.getProperty(propertyKey)));
        } finally {
            if (originalValue == null) {
                System.clearProperty(propertyKey);
            } else {
                System.setProperty(propertyKey, originalValue);
            }
        }
    }

    @Test
    void disableLogging() {
        outputProvider.disableLogging();
        assertTrue(true, "We expect no exception.");

        outputProvider = mock(DefaultOutputProvider.class);
        outputProvider.disableLogging();

        verify(outputProvider).disableLogging();
    }
}