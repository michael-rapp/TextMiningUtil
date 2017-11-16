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

import java.util.HashSet;
import java.util.Set;

import static de.mrapp.util.Condition.*;

/**
 * Allows to create n-grams from texts.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class NGramTokenizer {

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

    /**
     * Creates n-grams from a specific text.
     *
     * @param text The text, the n-grams should be created from, as a {@link String}. The text may
     *             neither be null, nor empty
     * @return A set, which contains the n-grams, which have been created, as an instance of the
     * type {@link Set}. The set may neither be null, nor empty
     */
    @NotNull
    public final Set<NGram> tokenize(final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        Set<NGram> nGrams = new HashSet<>();

        for (int n = minLength; n <= Math.min(text.length(), maxLength); n++) {
            for (int i = 0; i <= text.length() - n; i++) {
                String token = text.substring(i, i + n);
                nGrams.add(new NGram(token, i));
            }
        }

        return nGrams;
    }

}