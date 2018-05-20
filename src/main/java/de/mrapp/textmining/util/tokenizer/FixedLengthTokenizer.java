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

import java.util.*;

import static de.mrapp.util.Condition.*;

/**
 * Allows to split texts into substrings with a fixed length. E.g. given the length 2, the text
 * "text" is split into the substrings "te", "xt".
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public class FixedLengthTokenizer implements Tokenizer<FixedLengthTokenizer.Substring> {

    /**
     * A substring, which consists of a sequence of characters with a specific length, taken from a
     * longer text.
     */
    public static class Substring extends AbstractToken {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -5338654414796412975L;

        /**
         * Creates a new substring, which consists of a sequence of characters with a specific
         * length.
         *
         * @param token     The token of the substring as a {@link String}. The token may neither be
         *                  null, nor empty
         * @param positions A collection, which contains the position(s) of the substring's token in
         *                  the original text, as an instance of the type {@link Collection}. The
         *                  collection may not be null
         */
        private Substring(@NotNull final String token,
                          @NotNull final Collection<Integer> positions) {
            super(token, positions);
        }

        /**
         * Creates a new substring, which consists of a sequence of characters with a specific
         * length.
         *
         * @param token     The token of the substring as a {@link String}. The token may neither be
         *                  null, nor empty
         * @param positions An array, which contains the position(s) of the substring's token in the
         *                  original text as an {@link Integer} array. The array may neither be
         *                  null, nor empty
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

    /**
     * The length of the substrings, which are created by the tokenizer.
     */
    private final int length;


    /**
     * Creates a new tokenizer, which splits texts into substrings with a fixed length.
     *
     * @param length The length of the substrings, which should be created by the tokenizer, as an
     *               {@link Integer} value. The length must be at least 1
     */
    public FixedLengthTokenizer(final int length) {
        ensureAtLeast(length, 1, "The length must be at least 1");
        this.length = length;
    }

    /**
     * Returns the length of the substrings, which are created by the tokenizer.
     *
     * @return The length of the substrings, which are created by the tokenizer, as an {@link
     * Integer} value
     */
    public final int getLength() {
        return length;
    }

    @NotNull
    @Override
    public final Set<Substring> tokenize(@NotNull final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        ensureTrue(text.length() % length == 0,
                "The length of the text must be dividable by " + length);
        Map<String, Substring> substrings = new HashMap<>();
        int length = text.length();

        for (int i = 0; i <= length - this.length; i += this.length) {
            String token = text.substring(i, i + this.length);
            Substring substring = substrings.get(token);

            if (substring == null) {
                substring = new Substring(token, i);
                substrings.put(token, substring);
            } else {
                substring.addPosition(i);
            }
        }

        return new HashSet<>(substrings.values());
    }


}
