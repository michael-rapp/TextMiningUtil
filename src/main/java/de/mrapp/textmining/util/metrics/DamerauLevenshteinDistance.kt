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
class DamerauLevenshteinDistance : TextMetric {

    override fun evaluate(text1: CharSequence, text2: CharSequence): Double {
        val m = text1.length
        val n = text2.length

        when {
            m == 0 -> return n.toDouble()
            n == 0 -> return m.toDouble()
            else -> {
                val d = Array(m + 1) { IntArray(n + 1) }
                val characterMap = HashMap<Char, Int>()

                for (i in 0..m) {
                    d[i][0] = i
                }

                for (i in 0..n) {
                    d[0][i] = i
                }

                for (i in 1..m) {
                    val char1 = text1[i - 1]
                    var db = 0

                    for (j in 1..n) {
                        val char2 = text2[j - 1]
                        val i1 = characterMap.getOrDefault(char2, 0)
                        val j1 = db
                        var cost = 0

                        if (char1 == char2) {
                            db = j
                        } else {
                            cost = 1
                        }

                        var distance = Math.min(d[i][j - 1] + 1,
                                Math.min(d[i - 1][j] + 1, d[i - 1][j - 1] + cost))

                        if (i1 > 0 && j1 > 0) {
                            distance = Math.min(distance,
                                    d[i1 - 1][j1 - 1] + (i - i1 - 1) + (j - j1 - 1) + 1)
                        }

                        d[i][j] = distance
                    }

                    characterMap[char1] = i
                }

                return d[m][n].toDouble()
            }
        }
    }

    override fun minValue() = 0.0

    override fun maxValue() = Double.MAX_VALUE

    override fun isGainMetric() = false

}
