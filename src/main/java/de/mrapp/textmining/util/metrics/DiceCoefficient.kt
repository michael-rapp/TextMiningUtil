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

import de.mrapp.textmining.util.tokenizer.NGramTokenizer

/**
 * Allows to measure the similarity of texts according to the Dice coefficient. The dice coefficient
 * measures the percentage of n-grams, which occur in both texts. It always evaluates to a heuristic
 * value within the interval [0,1]. Texts that reach a greater heuristic value are considered to be
 * more similar than texts that reach a smaller heuristic value. The dice coefficient can also be
 * applied to texts with different lengths.
 *
 * @property minLength The minimum length of the n-grams that are taken into account by the metric
 * @property maxLength The maximum length of the n-grams that are taken into account by the metric
 * @author Michael Rapp
 * @since 1.0.0
 */
class DiceCoefficient(val minLength: Int = 1, val maxLength: Int = Int.MAX_VALUE) : TextMetric {

    private val nGramTokenizer: NGramTokenizer = NGramTokenizer(minLength, maxLength)

    override val minValue = 0.0

    override val maxValue = 1.0

    override val isGainMetric = true

    override fun evaluate(text1: CharSequence, text2: CharSequence): Double {
        val nGrams1 = nGramTokenizer.tokenize(text1)
        val nGrams2 = nGramTokenizer.tokenize(text2)
        val intersection = nGrams1.intersect(nGrams2)
        return (2 * intersection.size).toDouble() / (nGrams1.size + nGrams2.size).toDouble()
    }

}