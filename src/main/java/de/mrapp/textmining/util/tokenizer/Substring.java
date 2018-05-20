/*
 * Copyright 2017 Michael Rapp
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
package de.mrapp.textmining.util.tokenizer;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A substring, which consists of a sequence of characters, taken from a longer text.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public class Substring extends AbstractToken {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -179438678035961056L;

    /**
     * Creates a new substring, which consists of a sequence of characters, taken from a longer
     * text.
     *
     * @param token     The token of the substring as a {@link String}. The token may neither be
     *                  null, nor empty
     * @param positions A collection, which contains the position(s) of the substring's token in the
     *                  original text, as an instance of the type {@link Collection}. The collection
     *                  may not be null
     */
    private Substring(@NotNull final String token,
                      @NotNull final Collection<Integer> positions) {
        super(token, positions);
    }

    /**
     * Creates a new substring, which consists of a sequence of characters, taken from a longer
     * text.
     *
     * @param token     The token of the substring as a {@link String}. The token may neither be
     *                  null, nor empty
     * @param positions An array, which contains the position(s) of the substring's token in the
     *                  original text as an {@link Integer} array. The array may neither be null,
     *                  nor empty
     */
    public Substring(@NotNull final String token, @NotNull final int... positions) {
        super(token, positions);
    }

    @Override
    public final Substring clone() {
        return new Substring(getToken(), getPositions());
    }

    @Override
    public final String toString() {
        return "Substring [token=" + getToken() + ", positions=" + getPositions() + "]";
    }

}