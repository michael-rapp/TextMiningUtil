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

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * Allows to calculate the distance of texts according to the Levenshtein distance. The Levenshtein
 * distance measures the number of single-character edits (insertions, deletions or substitutions)
 * that are necessary to change one text into another. It can also used for texts with different
 * lengths.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class LevenshteinDistance implements TextMetric {

    @Override
    public final double evaluate(@NotNull final String text1, @NotNull final String text2) {
        ensureNotNull(text1, "The first text may not be null");
        ensureNotNull(text2, "The second text may not be null");
        int m = text1.length();
        int n = text2.length();

        if (m == 0) {
            return n;
        } else if (n == 0) {
            return m;
        } else {
            int[] v0 = new int[n + 1];
            int[] v1 = new int[n + 1];

            for (int i = 0; i <= n; i++) {
                v0[i] = i;
            }

            for (int i = 0; i < m; i++) {
                v1[0] = i + 1;

                for (int j = 0; j < n; j++) {
                    int substitutionCost = 1;

                    if (text1.charAt(i) == text2.charAt(j)) {
                        substitutionCost = 0;
                    }

                    v1[j + 1] = Math
                            .min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + substitutionCost);
                }

                int[] tmp = v1;
                v1 = v0;
                v0 = tmp;
            }

            return v0[n];
        }
    }

    @Override
    public final double minValue() {
        return 0;
    }

    @Override
    public final double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public final boolean isGainMetric() {
        return false;
    }

}