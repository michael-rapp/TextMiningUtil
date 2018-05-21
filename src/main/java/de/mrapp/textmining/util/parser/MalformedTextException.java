/*
 * Copyright 2017 - 2018 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.textmining.util.parser;

import org.jetbrains.annotations.NotNull;

import static de.mrapp.util.Condition.ensureNotEmpty;
import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An exception, which may be thrown by a {@link TextParser}, if a given text could be parsed.
 *
 * @author Michael Rapp
 * @since 1.3.0
 */
public class MalformedTextException extends Exception {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 8407576087556941465L;

    /**
     * The text, which could not be parsed.
     */
    private final String text;

    /**
     * Creates a new exception, which may be thrown by a {@link TextParser}, if a given text could
     * not be parsed.
     *
     * @param text    The text, which could not be parsed, as a {@link String}. The text may neither
     *                be null, nor empty
     * @param message The message of the exception as a {@link String}
     */
    public MalformedTextException(@NotNull final String text, final String message) {
        super(message);
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        this.text = text;
    }

    /**
     * Returns the text, which could not be parsed.
     *
     * @return The text, which could not be parsed, as a {@link String}. The text may neither be
     * null, nor empty
     */
    @NotNull
    public final String getText() {
        return text;
    }

}
