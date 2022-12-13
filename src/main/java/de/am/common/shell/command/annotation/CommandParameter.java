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
 * {@code CommandParameter} is used to annotate the parameters of {@link Command} marked method.<p>
 * You must at least provide name attribute, others being optional.
 *
 * @author Martin Absmeier
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameter {

    /**
     * The identification string of the command parameter.<p>
     * 1. Should reflect the original Java parameter name<p>
     * 2. Should be short and descriptive to the user<p>
     * <b>Recommendations:</b> "number-of-nodes", "user-login", "coefficients".
     *
     * @return the name of the command parameter or empty string "" if not set
     */
    String name() default "";

    /**
     * The description of the command parameter.<p>
     * Default description (if this property is not set) says "methodName(Arg1Type, Arg2Type,...) : ReturnType".
     *
     * @return the description of the command parameter or empty string "" if not set
     */
    String description() default "";
}