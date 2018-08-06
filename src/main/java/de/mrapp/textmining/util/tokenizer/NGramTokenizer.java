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
package de.mrapp.textmining.util.tokenizer;

import de.mrapp.textmining.util.AbstractToken;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

import static de.mrapp.util.Condition.ensureAtLeast;

/**
 * Allows to create n-grams from texts. E.g. the substrings "t", "te", "tex", "ext", "xt" and "t"
 * can be created from the text "text". In general, the {@link NGramTokenizer} splits texts into
 * less tokens than the class {@link SubstringTokenizer}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class NGramTokenizer extends AbstractTokenizer<NGramTokenizer.NGram> {

    /**
     * A n-Gram, which consists of a sequence of characters, taken from a longer text or word.
     */
    public static class NGram extends AbstractToken {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -5907278359683181663L;

        /**
         * The degree of the n-grams, which are created by the tokenizer, which created this
         * n-gram.
         */
        private final int n;

        /**
         * Creates a new n-gram, which consists of a sequence of characters.
         *
         * @param n         The degree of the n-grams, which are created by the tokenizer, which
         *                  created this n-gram, as an {@link Integer} value
         * @param token     The token of the n-gram as a {@link String}. The token may neither be
         *                  null, nor empty
         * @param positions A collection, which contains the position(s) of the n-gram's token in
         *                  the original text, as an instance of the type {@link Collection}. The
         *                  collection may not be null
         */
        private NGram(final int n, @NotNull final String token,
                      @NotNull final Collection<Integer> positions) {
            super(token, positions);
            ensureAtLeast(n, 1, "The degree of the n-gram must be at least 1");
            this.n = n;
        }

        /**
         * Creates a new n-gram, which consists of a sequence of characters.
         *
         * @param n         The degree of the n-grams, which are created by the tokenizer, which
         *                  created this n-gram, as an {@link Integer} value
         * @param token     The token of the n-gram as a {@link String}. The token may neither be
         *                  null, nor empty
         * @param positions An array, which contains the position(s) of the n-gram's token in the
         *                  original text as an {@link Integer} array. The array may neither be
         *                  null, nor empty
         */
        public NGram(final int n, @NotNull final String token, @NotNull final int... positions) {
            super(token, positions);
            ensureAtLeast(n, 1, "The degree of the n-gram must be at least 1");
            this.n = n;
        }

        /**
         * Returns the degree of the n-grams, which are created by the tokenizer, which created this
         * n-gram.
         *
         * @return The degree of the n-grams as an {@link Integer} value
         */
        public final int getN() {
            return n;
        }

        @NotNull
        @Override
        public final NGram clone() {
            return new NGram(getN(), getToken(), getPositions());
        }

        @Override
        public final String toString() {
            return "NGram [n=" + n + ", token=" + getToken() + ", positions=" + getPositions() +
                    "]";
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + n;
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
            if (!super.equals(obj))
                return false;
            NGram other = (NGram) obj;
            if (n != other.n)
                return false;
            return true;
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

    @Override
    protected final void onTokenize(@NotNull final String text,
                                    @NotNull final Map<String, NGram> tokens) {
        int length = text.length();
        BiFunction<String, Integer, NGram> nGramFactory = (token, position) ->
                new NGram(getMaxLength(), token, position);

        for (int n = minLength; n <= Math.min(maxLength, length - 1); n++) {
            String token = text.substring(0, n);
            addToken(tokens, token, 0, nGramFactory);
        }

        for (int i = 1; i <= length - minLength; i++) {
            String token = text.substring(i, i + Math.min(maxLength, length - i));
            addToken(tokens, token, i, nGramFactory);
        }
    }

}