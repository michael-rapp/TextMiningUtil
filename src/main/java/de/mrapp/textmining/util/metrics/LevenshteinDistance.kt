/*
 * Copyright 2017 - 2019 Michael Rapp
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
 * Allows to calculate the distance of texts according to the Levenshtein distance. The Levenshtein
 * distance measures the number of single-character edits (insertions, deletions or substitutions)
 * that are necessary to change one text into another. It can also be used for texts with different
 * lengths.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
class LevenshteinDistance : TextMetric {

    override fun evaluate(text1: CharSequence, text2: CharSequence): Double {
        val m = text1.length
        val n = text2.length

        when {
            m == 0 -> return n.toDouble()
            n == 0 -> return m.toDouble()
            else -> {
                var v0 = IntArray(n + 1)
                var v1 = IntArray(n + 1)

                for (i in 0..n) {
                    v0[i] = i
                }

                for (i in 0 until m) {
                    v1[0] = i + 1

                    for (j in 0 until n) {
                        var substitutionCost = 1

                        if (text1[i] == text2[j]) {
                            substitutionCost = 0
                        }

                        v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + substitutionCost)
                    }

                    val tmp = v1
                    v1 = v0
                    v0 = tmp
                }

                return v0[n].toDouble()
            }
        }
    }

    override fun minValue() = 0.0

    override fun maxValue() = Double.MAX_VALUE

    override fun isGainMetric() = false

}