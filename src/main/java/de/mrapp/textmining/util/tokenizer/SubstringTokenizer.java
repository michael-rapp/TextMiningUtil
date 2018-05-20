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
 * Allows to create substrings from texts. E.g. the substrings "t", "e", "x", "t", "te", "ex", "xt",
 * "tex" and "ext" can be created from the text "text".
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SubstringTokenizer implements Tokenizer<SubstringTokenizer.Substring> {

    /**
     * A substring, which consists of a sequence of characters, taken from a longer text.
     */
    public static class Substring extends AbstractToken {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -5907278359683181663L;

        /**
         * Creates a new substring, which consists of a sequence of characters.
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
         * Creates a new substring, which consists of a sequence of characters.
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
     * The minimum length of the substrings, which are created by the tokenizer.
     */
    private final int minLength;

    /**
     * The maximum length of the substrings, which are created by the tokenizer.
     */
    private final int maxLength;

    /**
     * Creates a new tokenizer, which creates substrings with all possible lengths.
     */
    public SubstringTokenizer() {
        this(1, Integer.MAX_VALUE);
    }

    /**
     * Creates a new tokenizer, which creates substrings with a specific minimum and maximum
     * length.
     *
     * @param minLength The minimum length of the substrings, which should be created by the
     *                  tokenizer, as an {@link Integer} value. The minimum length must be at least
     *                  1
     * @param maxLength The maximum length of the substrings, which should be created by the
     *                  tokenizer, as an {@link Integer} value. The maximum length must be at least
     *                  the minimum length
     */
    public SubstringTokenizer(final int minLength, final int maxLength) {
        ensureAtLeast(minLength, 1, "The minimum length must be at least 1");
        ensureAtLeast(maxLength, minLength,
                "The maximum length must be at least the minimum length");
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Returns the minimum length of the substrings, which are created by the tokenizer.
     *
     * @return The minimum length of the substrings, which are created by the tokenizer, as an
     * {@link Integer} value
     */
    public final int getMinLength() {
        return minLength;
    }

    /**
     * Returns the maximum length of the substrings, which are created by the tokenizer.
     *
     * @return The maximum length of the substrings, which are created by the tokenizer, as an
     * {@link Integer} value
     */
    public final int getMaxLength() {
        return maxLength;
    }

    @NotNull
    @Override
    public final Set<Substring> tokenize(@NotNull final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        Map<String, Substring> substrings = new HashMap<>();
        int length = text.length();

        for (int n = minLength; n <= Math.min(length - 1, maxLength); n++) {
            for (int i = 0; i <= length - n; i++) {
                String token = text.substring(i, i + n);
                Substring substring = substrings.get(token);

                if (substring == null) {
                    substring = new Substring(token, i);
                    substrings.put(token, substring);
                } else {
                    substring.addPosition(i);
                }
            }
        }

        return new HashSet<>(substrings.values());
    }

}