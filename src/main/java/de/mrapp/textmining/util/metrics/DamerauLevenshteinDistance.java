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

import java.util.HashMap;
import java.util.Map;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * Allows to calculate the distance between texts according to the Damerau-Levenshtein distance
 * (also referred to as "unrestricted edit distance"). The Damerau-Levenshtein distance measures the
 * number of single-character edits (insertions, deletions, substitutions) and transposition of
 * adjacent characters that are needed to make the texts equal to each other. Unlike the optimal
 * string alignment distance, the Damerau-Levensthein distance does not have any restrictions and
 * does meet the properties of the triangle inequality.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public class DamerauLevenshteinDistance implements TextMetric {

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
            int[][] d = new int[m + 1][n + 1];
            Map<Character, Integer> characterMap = new HashMap<>();

            for (int i = 0; i <= m; i++) {
                d[i][0] = i;
            }

            for (int i = 0; i <= n; i++) {
                d[0][i] = i;
            }

            for (int i = 1; i <= m; i++) {
                char char1 = text1.charAt(i - 1);
                int db = 0;

                for (int j = 1; j <= n; j++) {
                    char char2 = text2.charAt(j - 1);
                    int i1 = characterMap.getOrDefault(char2, 0);
                    int j1 = db;
                    int cost = 0;

                    if (char1 == char2) {
                        db = j;
                    } else {
                        cost = 1;
                    }

                    int distance = Math.min(d[i][j - 1] + 1,
                            Math.min(d[i - 1][j] + 1, d[i - 1][j - 1] + cost));

                    if (i1 > 0 && j1 > 0) {
                        distance = Math
                                .min(distance, d[i1 - 1][j1 - 1] + (i - i1 - 1) + (j - j1 - 1) + 1);
                    }

                    d[i][j] = distance;
                }

                characterMap.put(char1, i);
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
