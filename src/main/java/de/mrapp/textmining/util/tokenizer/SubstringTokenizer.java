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

import java.util.Map;

import static de.mrapp.util.Condition.ensureAtLeast;

/**
 * Allows to create substrings from texts. E.g. the substrings "t", "e", "x", "t", "te", "ex", "xt",
 * "tex" and "ext" can be created from the text "text".
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SubstringTokenizer extends AbstractTokenizer<Substring> {

    /*
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

    @Override
    protected final void onTokenize(@NotNull final String text,
                                    @NotNull final Map<String, Substring> tokens) {
        int length = text.length();

        for (int n = minLength; n <= Math.min(length - 1, maxLength); n++) {
            for (int i = 0; i <= length - n; i++) {
                String token = text.substring(i, i + n);
                addToken(tokens, token, i, Substring::new);
            }
        }
    }

}