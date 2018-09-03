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
package de.mrapp.textmining.util.metrics

/**
 * Allows to calculate the distance between texts according to the optimal string alignment distance
 * (abbreviated as OSA distance, also referred to as "restricted edit distance" or "restricted
 * Damerau-Levenshtein distance"). The OSA distance measures the number of single-character edits
 * (insertions, deletions, substitutions) and transpositions of two adjacent characters that are
 * needed to make the texts equal to each other. Unlike the Damerau-Levensthein distance, the OSA
 * distance does not allow substrings to be edited more than once. Furthermore, it does not meet the
 * properties of the triangle inequality.
 *
 * In natural language processing words are often short and misspellings usually do not affect more
 * than 2 characters. Therefore, - despite its limitations - the OSA distance can be used as an
 * efficient approximation of the unrestricted Damerau-Levenshtein distance in such scenarios.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
class OptimalStringAlignmentDistance : TextMetric {

    override fun evaluate(text1: CharSequence, text2: CharSequence): Double {
        val m = text1.length
        val n = text2.length

        when {
            m == 0 -> return n.toDouble()
            n == 0 -> return m.toDouble()
            else -> {
                val d = Array(m + 1) { IntArray(n + 1) }

                for (i in 0..m) {
                    d[i][0] = i
                }

                for (i in 0..n) {
                    d[0][i] = i
                }

                for (i in 1..m) {
                    for (j in 1..n) {
                        val char1 = text1[i - 1]
                        val char2 = text2[j - 1]
                        val cost = if (char1 == char2) 0 else 1
                        var distance = Math.min(d[i][j - 1] + 1,
                                Math.min(d[i - 1][j] + 1, d[i - 1][j - 1] + cost))

                        if (i > 1 && j > 1 && char1 == text2[j - 2] && text1[i - 2] == char2) {
                            distance = Math.min(distance, d[i - 2][j - 2] + cost)
                        }

                        d[i][j] = distance
                    }
                }

                return d[m][n].toDouble()
            }
        }
    }

    override fun minValue() = 0.0

    override fun maxValue() = Double.MAX_VALUE

    override fun isGainMetric() = false

}
