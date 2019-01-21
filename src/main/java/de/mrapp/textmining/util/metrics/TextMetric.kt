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
 * Defines the interface, a metric, which allows to calculate heuristic values, which specify the
 * similarity or dissimilarity of texts, must implement.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
interface TextMetric {

    companion object {

        /**
         * Creates and returns a case-insensitive metric from an existing [metric].
         */
        fun caseInsensitive(metric: TextMetric): TextMetric {
            return object : TextMetric {

                override val minValue = metric.minValue

                override val maxValue = metric.maxValue

                override val isGainMetric = metric.isGainMetric

                override fun evaluate(text1: CharSequence, text2: CharSequence) =
                        metric.evaluate(text1.toString().toLowerCase(),
                                text2.toString().toLowerCase())

            }
        }

    }

    /**
     * The minimum heuristic value, which is possibly calculated by the metric.
     */
    val minValue: Double

    /**
     * The maximum heuristic value, which is possibly calculated by the metric.
     */
    val maxValue: Double

    /**
     * True, if the metric is a gain metric, i.e. that two strings that evaluate to a greater
     * heuristic value are considered to be more similar than two strings that evaluate to a smaller
     * heuristic value, false otherwise.
     */
    val isGainMetric: Boolean

    /**
     * True, if the metric is a loss metric, i.e. that two strings that evaluate to a smaller
     * heuristic value are considered to be more similar than two strings that evaluate to a greater
     * heuristic value, false otherwise.
     */
    val isLossMetric: Boolean
        get() = !isGainMetric

    /**
     * Calculates the heuristic value of two texts, [text1] and [text2], according to the metric.
     *
     * @return The heuristic value, which has been calculated. The heuristic value is at least
     * [minValue] and at maximum [maxValue]
     */
    fun evaluate(text1: CharSequence, text2: CharSequence): Double

}