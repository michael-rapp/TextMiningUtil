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

    @NotNull
    public final Set<NGram> tokenize(final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        return tokenize(text, 1, text.length());
    }

    @NotNull
    public final Set<NGram> tokenize(@NotNull final String text, final int minLength,
                                     final int maxLength) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        ensureAtLeast(minLength, 1, "The minimum length must be at least 1");
        ensureAtMaximum(maxLength, text.length(),
                "The maximum length must be at maximum " + text.length());
        ensureAtMaximum(minLength, maxLength,
                "The minimum length must be at maximum the maximum length");
        Set<NGram> nGrams = new HashSet<>();

        for (int n = minLength; n <= maxLength; n++) {
            for (int i = 0; i <= text.length() - n; i++) {
                String token = text.substring(i, i + n);
                nGrams.add(new NGram(token, i));
            }
        }

        return nGrams;
    }

}