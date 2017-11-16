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

import org.jetbrains.annotations.NotNull;

/**
 * Hamming dissimilarity is the loss metric pendant to the {@link LevenshteinDistance}. It measures
 * the percentage of single-character edits (insertions, deletions, or substitutions) that are
 * necessary to change one text into another among the number of characters of the longer of both
 * texts. Hamming dissimilarity always evaluates to heuristic values within the interval [0,1].
 * Texts that evaluate to greater heuristic values are considered to be less similar than texts that
 * evaluate to smaller heuristic values.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class LevenshteinDissimilarity implements TextMetric {

    @Override
    public final double evaluate(@NotNull final String text1, @NotNull final String text2) {
        double levenshteinDistance = new LevenshteinDistance().evaluate(text1, text2);
        int length = Math.max(text1.length(), text2.length());
        return length != 0 ? levenshteinDistance / (double) length : 0;
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
        return false;
    }

}