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
 * Allows to calculate the distance between texts according to the optimal string alignment distance
 * (abbreviated as OSA distance, also referred to as "restricted edit distance" or "restricted
 * Damerau-Levenshtein distance"). The OSA distance measures the number of single-character edits
 * (insertions, deletions, substitutions) and transpositions of two adjacent characters that are
 * needed to make the texts equal to each other. Unlike the Damerau-Levensthein distance, the OSA
 * distance does not allow substrings to be edited more than once. Furthermore, it does not meet the
 * properties of the triangle inequality.
 * <p>
 * In natural language processing words are often short and misspellings usually do not affect more
 * than 2 characters. Therefore, - despite its limitations - the OSA distance can be used as an
 * efficient approximation of the unrestricted Damerau-Levenshtein distance in such scenarios.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public class OptimalStringAlignmentDistance implements TextMetric {

    @Override
    public double evaluate(@NotNull final String text1, @NotNull final String text2) {
        ensureNotNull(text1, "The first text may not be null");
        ensureNotNull(text2, "The second text may not be null");
        int m = text1.length();
        int n = text2.length();

        if (m == 0) {
            return n;
        } else if (n == 0) {
            return m;
        } else {
            int[][] d = new int[m + 1][n + 1];

            for (int i = 0; i <= m; i++) {
                d[i][0] = i;
            }

            for (int i = 0; i <= n; i++) {
                d[0][i] = i;
            }

            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    char char1 = text1.charAt(i - 1);
                    char char2 = text2.charAt(j - 1);
                    int cost = char1 == char2 ? 0 : 1;
                    int distance = Math.min(d[i][j - 1] + 1,
                            Math.min(d[i - 1][j] + 1, d[i - 1][j - 1] + cost));

                    if (i > 1 && j > 1 && char1 == text2.charAt(j - 2) &&
                            text1.charAt(i - 2) == char2) {
                        distance = Math.min(distance, d[i - 2][j - 2] + cost);
                    }

                    d[i][j] = distance;
                }
            }

            return d[m][n];
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
