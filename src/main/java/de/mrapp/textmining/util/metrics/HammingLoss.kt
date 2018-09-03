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
 * Hamming loss is the loss metric pendant to the [HammingDistance]. It measures the percentage of
 * characters at corresponding positions that are not equal. Hamming loss always evaluates to
 * heuristic values in the interval [0,1]. Texts that evaluate to a greater heuristic value are
 * considered to be more dissimilar than texts that evaluate to smaller values.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
class HammingLoss : TextMetric {

    private val hammingDistance = HammingDistance()

    override fun evaluate(text1: CharSequence, text2: CharSequence): Double {
        val hammingDistance = hammingDistance.evaluate(text1, text2)
        val length = text1.length
        return if (length != 0) hammingDistance / length.toDouble() else 0.0
    }

    override fun minValue() = 0.0

    override fun maxValue() = 1.0

    override fun isGainMetric() = false

}