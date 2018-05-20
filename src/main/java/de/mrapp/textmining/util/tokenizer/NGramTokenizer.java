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
 * Allows to create n-grams from texts. E.g. the substrings "t", "te", "tex", "ext", "xt" and "t"
 * can be created from the text "text". In general, the {@link NGramTokenizer} splits texts into
 * less tokens than the class {@link SubstringTokenizer}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class NGramTokenizer implements Tokenizer<NGramTokenizer.NGram> {

    /**
     * A n-Gram, which consists of a sequence of characters, taken from a longer text or word.
     */
    public static class NGram extends AbstractToken {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -5907278359683181663L;

        /**
         * Creates a new n-gram, which consists of a sequence of characters.
         *
         * @param token     The token of the n-gram as a {@link String}. The token may neither be
         *                  null, nor empty
         * @param positions A collection, which contains the position(s) of the n-gram's token in
         *                  the original text, as an instance of the type {@link Collection}. The
         *                  collection may not be null
         */
        private NGram(@NotNull final String token, @NotNull final Collection<Integer> positions) {
            super(token, positions);
        }

        /**
         * Creates a new n-gram, which consists of a sequence of characters.
         *
         * @param token     The token of the n-gram as a {@link String}. The token may neither be
         *                  null, nor empty
         * @param positions An array, which contains the position(s) of the n-gram's token in the
         *                  original text as an {@link Integer} array. The array may neither be
         *                  null, nor empty
         */
        public NGram(@NotNull final String token, @NotNull final int... positions) {
            super(token, positions);
        }

        @Override
        public final NGram clone() {
            return new NGram(getToken(), getPositions());
        }

        @Override
        public final String toString() {
            return "NGram [token=" + getToken() + ", positions=" + getPositions() + "]";
        }

    }

    /**
     * The minimum length of the n-grams, which are created by the tokenizer.
     */
    private final int minLength;

    /**
     * The maximum length of the n-grams, which are created by the tokenizer.
     */
    private final int maxLength;

    /**
     * Adds a new n-gram to a map, if it is not already contained. Otherwise, the n-grams position
     * is added to the existing n-gram.
     *
     * @param nGrams   A map, which contains n-grams mapped to their tokens, as an instance of the
     *                 type {@link Map}. The map may not be null
     * @param token    The token of the n-gram, which should be created, as a {@link String}
     * @param position The position of the n-gram, which should be created, as an {@link Integer}
     *                 value
     */
    private void addNGram(final Map<String, NGram> nGrams, final String token, final int position) {
        NGram nGram = nGrams.get(token);

        if (nGram == null) {
            nGram = new NGram(token, position);
            nGrams.put(token, nGram);
        } else {
            nGram.addPosition(position);
        }
    }

    /**
     * Creates a new tokenizer, which creates n-grams with all possible lengths.
     */
    public NGramTokenizer() {
        this(1, Integer.MAX_VALUE);
    }

    /**
     * Creates a new tokenizer, which creates n-grams with a specific maximum length.
     *
     * @param maxLength The maximum length of the n-grams, which should be created by the tokenizer,
     *                  as an {@link Integer} value. The maximum length must be at least 1
     */
    public NGramTokenizer(final int maxLength) {
        this(1, maxLength);
    }

    /**
     * Creates a new tokenizer, which creates n-grams with a specific minimum and maximum length.
     *
     * @param minLength The minimum length of the n-grams, which should be created by the tokenizer,
     *                  as an {@link Integer} value. The minimum length must be at least 1
     * @param maxLength The maximum length of the n-grams, which should be created by the tokenizer,
     *                  as an {@link Integer} value. The maximum length must be at least the minimum
     *                  length
     */
    public NGramTokenizer(final int minLength, final int maxLength) {
        ensureAtLeast(minLength, 1, "The minimum length must be at least 1");
        ensureAtLeast(maxLength, minLength,
                "The maximum length must be at least the minimum length");
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Returns the minimum length of the n-grams, which are created by the tokenizer.
     *
     * @return The minimum length of the n-grams, which are created by the tokenizer, as an {@link
     * Integer} value
     */
    public final int getMinLength() {
        return minLength;
    }

    /**
     * Returns the maximum length of the n-grams, which are created by the tokenizer.
     *
     * @return The maximum length of the n-grams, which are created by the tokenizer, as an {@link
     * Integer} value
     */
    public final int getMaxLength() {
        return maxLength;
    }

    @NotNull
    @Override
    public final Set<NGram> tokenize(@NotNull final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        Map<String, NGram> nGrams = new HashMap<>();
        int length = text.length();

        for (int n = minLength; n <= Math.min(maxLength, length - 1); n++) {
            String token = text.substring(0, n);
            addNGram(nGrams, token, 0);
        }

        for (int i = 1; i <= length - minLength; i++) {
            String token = text.substring(i, i + Math.min(maxLength, length - i));
            addNGram(nGrams, token, i);
        }

        return new HashSet<>(nGrams.values());
    }

}