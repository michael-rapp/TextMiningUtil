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
 * Damerau-Levenshtein dissimilarity is the loss metric pendant to the [DamerauLevenshteinDistance].
 * It measures the percentage of single-character edits (insertions, deletions, or substitutions)
 * and transpositions of adjacent characters that are necessary to change one text into another,
 * among the number of characters of the longer of both texts. Damerau-Levenshtein dissimilarity
 * always evaluates to heuristic values within the interval [0,1]. Texts that evaluate to greater
 * heuristic values are considered to be less similar than texts that evaluate to smaller heuristic
 * values.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
class DamerauLevenshteinDissimilarity : TextMetric {

    private val damerauLevenshteinDistance = DamerauLevenshteinDistance()

    override fun evaluate(text1: CharSequence, text2: CharSequence): Double {
        val damerauLevenshteinDistance = damerauLevenshteinDistance.evaluate(text1, text2)
        val length = Math.max(text1.length, text2.length)
        return if (length != 0) damerauLevenshteinDistance / length.toDouble() else 0.0
    }

    override fun minValue() = 0.0

    override fun maxValue() = 1.0

    override fun isGainMetric() = false

}
