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

import de.mrapp.util.Condition.ensureEqual

/**
 * Allows to calculate the distance of texts according to the Hamming distance. The Hamming distance
 * is defined as the number of characters at corresponding positions that are not equal. It can only
 * be used for texts with the same length.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
class HammingDistance : TextMetric {

    override fun evaluate(text1: CharSequence, text2: CharSequence): Double {
        val length1 = text1.length
        val length2 = text2.length
        ensureEqual(length1, length2, "The texts must have the same length")
        var distance = 0.0

        for (i in 0 until length1) {
            if (text1[i] != text2[i]) {
                distance++
            }
        }

        return distance
    }

    override fun minValue() = 0.0

    override fun maxValue() = Double.MAX_VALUE

    override fun isGainMetric() = false

}