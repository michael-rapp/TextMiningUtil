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
package de.mrapp.textmining.util.ngram;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static de.mrapp.util.Condition.*;

/**
 * Allows to create substrings from texts. E.g. the substrings "t", "e", "x", "t", "te", "ex", "xt",
 * "tex" and "ext" can be created from the text "text".
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SubstringTokenizer {

    /**
     * A substring, which consists of a sequence of characters (token), taken from a longer text or
     * word.
     */
    public static class Substring implements Serializable, Cloneable {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -5907278359683181663L;

        /**
         * The substring's token.
         */
        private final String token;

        /**
         * The position of the substring's token in the original text or word.
         */
        private final int position;

        /**
         * Creates a new substring, which consists of a sequence of characters (token).
         *
         * @param token    The token of the substring as a {@link String}. The token may neither be
         *                 null, nor empty
         * @param position The position of the substring's token in the original text or word as an
         *                 {@link Integer} value. The position must be at least 0
         */
        public Substring(@NotNull final String token, final int position) {
            ensureNotNull(token, "The token may not be null");
            ensureNotEmpty(token, "The token may not be null");
            ensureAtLeast(position, 0, "The position must be at least 0");
            this.token = token;
            this.position = position;
        }

        /**
         * Returns the token of the substring.
         *
         * @return The token of the substring as a {@link String}. The token may neither be null,
         * nor empty
         */
        @NotNull
        public final String getToken() {
            return token;
        }

        /**
         * Returns the position of the substring's token in the original text or word.
         *
         * @return The position of the substring's token as an {@link Integer} value. The position
         * must be at least 0
         */
        public final int getPosition() {
            return position;
        }

        /**
         * Returns the length of the substring's token.
         *
         * @return The length of the substring's token as an {@link Integer} value
         */
        public final int length() {
            return token.length();
        }

        @Override
        public final Substring clone() {
            return new Substring(token, position);
        }

        @Override
        public final String toString() {
            return "Substring [token=" + token + ", position=" + position + "]";
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + token.hashCode();
            result = prime * result + position;
            return result;
        }

        @Override
        public final boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Substring other = (Substring) obj;
            return token.equals(other.token) && position == other.position;
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

    /**
     * Creates substrings from a specific text.
     *
     * @param text The text, the substrings should be created from, as a {@link String}. The text
     *             may neither be null, nor empty
     * @return A set, which contains the substrings, which have been created, as an instance of the
     * type {@link Set}. The set may neither be null, nor empty
     */
    @NotNull
    public final Set<Substring> tokenize(final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        Set<Substring> substrings = new HashSet<>();

        for (int n = minLength; n <= Math.min(text.length() - 1, maxLength); n++) {
            for (int i = 0; i <= text.length() - n; i++) {
                String token = text.substring(i, i + n);
                substrings.add(new Substring(token, i));
            }
        }

        return substrings;
    }

}