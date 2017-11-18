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
package de.mrapp.textmining.util.metrics;

import de.mrapp.textmining.util.tokenizer.NGramTokenizer;
import de.mrapp.textmining.util.tokenizer.NGramTokenizer.NGram;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Allows to measure the similarity of texts according to the Dice coefficient. The dice coefficient
 * measures the percentage of n-grams, which occur in both texts. It always evaluates to a heuristic
 * value within the interval [0,1]. Texts that reach a greater heuristic value are considered to be
 * more similar than texts that reach a smaller heuristic value. The dice coefficient can also be
 * applied to texts with different lengths.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class DiceCoefficient implements TextMetric {

    private final NGramTokenizer nGramTokenizer;

    /**
     * Creates a new dice coefficient, which is calculated based on all n-grams, which can be
     * created from texts, regardless of their length.
     */
    public DiceCoefficient() {
        this.nGramTokenizer = new NGramTokenizer();
    }

    /**
     * Creates a new dice coefficient, which is calculated based on n-grams with a specific maximum
     * length.
     *
     * @param maxLength The maximum length of the n-grams, which should be taken into account, as an
     *                  {@link Integer} value. The maximum length must be at least 1
     */
    public DiceCoefficient(final int maxLength) {
        this.nGramTokenizer = new NGramTokenizer(maxLength);
    }

    /**
     * Creates a new dice coefficient, which is calculated based on n-grams with a specific minimum
     * and maximum length.
     *
     * @param minLength The minimum length of the n-grams, which should be taken into account, as an
     *                  {@link Integer} value. The minimum length must be at least 1
     * @param maxLength The maximum length of the n-grams, which should be taken into account, as an
     *                  {@link Integer} value. The maximum length must at least the minimum length
     */
    public DiceCoefficient(final int minLength, final int maxLength) {
        this.nGramTokenizer = new NGramTokenizer(minLength, maxLength);
    }

    /**
     * Returns the minimum length of the n-grams, which are taken into account by the metric.
     *
     * @return The minimum length of the n-grams, which are taken into account by the metric, as an
     * {@link Integer} value
     */
    public final int getMinLength() {
        return nGramTokenizer.getMinLength();
    }

    /**
     * Returns the maximum length of the n-grams, which are taken into account by the metric.
     *
     * @return The maximum length of the n-grams, which are taken into account by the metric, as an
     * {@link Integer} value
     */
    public final int getMaxLength() {
        return nGramTokenizer.getMaxLength();
    }

    @Override
    public final double evaluate(@NotNull final String text1, @NotNull final String text2) {
        Set<NGram> nGrams1 = nGramTokenizer.tokenize(text1);
        Set<NGram> nGrams2 = nGramTokenizer.tokenize(text2);
        long intersection = nGrams1.stream().filter(nGrams2::contains).count();
        return (double) (2 * intersection) / (double) (nGrams1.size() + nGrams2.size());
    }

    @Override
    public final double minValue() {
        return 0;
    }

    @Override
    public final double maxValue() {
        return 1;
    }

    @Override
    public final boolean isGainMetric() {
        return true;
    }

}