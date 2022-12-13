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
package de.am.common.shell.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code Command} is used to annotate methods to be recognized as a command.
 * If the command name is not set, the name of the annotated method is used as the command name.
 * All attributes are optional
 *
 * @author Martin Absmeier
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * The identification string of the command.<p>
     * Allows overriding default command name, which is derived from method name.
     *
     * @return the name or empty string "" if not set
     */
    String name() default "";

    /**
     * The description of the command.<p>
     * Default description (if this property is not set) says "methodName(Arg1Type, Arg2Type,...) : ReturnType".
     *
     * @return the description or empty string "" if not set
     */
    String description() default "";

    /**
     * The shortcut name of the command.<p>
     * If not set and, the name attribute is not set as well, the Shell takes
     * the first letter of each word (void selectUser() --- select-user --- su).
     *
     * @return the shortcut or empty string "" if not set
     */
    String shortcut() default "";
}