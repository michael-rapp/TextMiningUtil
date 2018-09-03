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
package de.mrapp.textmining.util.parser

import de.mrapp.textmining.util.metrics.TextMetric
import de.mrapp.util.Condition.ensureAtLeast
import de.mrapp.util.Condition.ensureAtMaximum
import java.util.regex.Pattern


/**
 * Defines the interface, a class that allows to check, whether two value with types [F] and [S]
 * match, or not, must implement.
 *
 * @param F The type of the first value
 * @param S The type of the second value
 * @author Michael Rapp
 * @since 1.3.0
 */
interface Matcher<F, S> {

    companion object {

        /**
         * Creates and returns a matcher that allows to check whether texts are equal, or not.
         *
         * @param ignoreCase True, if the matcher should be case-insensitive, false otherwise
         */
        @JvmOverloads
        fun <F : CharSequence, S : CharSequence> equals(ignoreCase: Boolean = false):
                Matcher<F, S> {
            return object : Matcher<F, S> {

                override fun getMatch(first: F, second: S) =
                        if (first.toString().equals(second.toString(), ignoreCase))
                            Match(first, second, 1.0) else null

                override fun isGainMetric() = true

            }
        }

        /**
         * Creates and returns a matcher that allows to check whether texts start with certain
         * prefixes, or not.
         *
         * @param ignoreCase True, if the matcher should be case-insensitive, false otherwise
         */
        @JvmOverloads
        fun <F : CharSequence, S : CharSequence> startsWith(ignoreCase: Boolean = false):
                Matcher<F, S> {
            return object : Matcher<F, S> {

                override fun getMatch(first: F, second: S) =
                        if (first.toString().startsWith(second.toString(), ignoreCase))
                            Match(first, second, 1.0) else null

                override fun isGainMetric() = true

            }
        }

        /**
         * Creates and returns a matcher that allows to check whether texts end with certain
         * suffixes, or not.
         *
         * @param ignoreCase True, if the matcher should be case-insensitive, false otherwise
         */
        @JvmOverloads
        fun <F : CharSequence, S : CharSequence> endsWith(ignoreCase: Boolean = false):
                Matcher<F, S> {
            return object : Matcher<F, S> {

                override fun getMatch(first: F, second: S) =
                        if (first.toString().endsWith(second.toString(), ignoreCase))
                            Match(first, second, 1.0) else null

                override fun isGainMetric() = true

            }
        }

        /**
         * Creates and returns a matcher that allows to check whether texts contain certain
         * subtexts, or not.
         *
         * @param ignoreCase True, if the matcher should be case-insensitive, false otherwise
         */
        @JvmOverloads
        fun <F : CharSequence, S : CharSequence> contains(ignoreCase: Boolean = false):
                Matcher<F, S> {
            return object : Matcher<F, S> {

                override fun getMatch(first: F, second: S) =
                        if (first.toString().contains(second.toString(), ignoreCase))
                            Match(first, second, 1.0) else null

                override fun isGainMetric() = true

            }
        }

        /**
         * Creates and returns a matcher that allows to check whether texts match a certain pattern,
         * or not.
         */
        fun <F : CharSequence> pattern(): Matcher<F, Pattern> {
            return object : Matcher<F, Pattern> {

                override fun getMatch(first: F, second: Pattern) =
                        if (second.matcher(first.toString()).matches()) Match(first, second, 1.0)
                        else null

                override fun isGainMetric() = true

            }
        }

        /**
         * Creates and returns a matcher that allows to check whether texts match according to a
         * given [metric] and [threshold], or not.
         */
        fun <F : CharSequence, S : CharSequence> metric(metric: TextMetric, threshold: Double):
                Matcher<F, S> {
            ensureAtLeast(threshold, metric.minValue(),
                    "The threshold must be at least ${metric.minValue()}")
            ensureAtMaximum(threshold, metric.maxValue(),
                    "The threshold must be at maximum ${metric.maxValue()}")
            return object : Matcher<F, S> {

                override fun getMatch(first: F, second: S): Match<F, S>? {
                    val heuristicValue = metric.evaluate(first.toString(), second.toString())
                    val matches = if (metric.isGainMetric()) heuristicValue >= threshold else
                        heuristicValue <= threshold
                    return if (matches) Match(first, second, heuristicValue) else null
                }

                override fun isGainMetric() = metric.isGainMetric()

            }
        }

    }

    /**
     * Returns, whether two values [first] and [second] match each other according to the matcher,
     * or not.
     */
    fun matches(first: F, second: S) = getMatch(first, second) != null

    /**
     * Returns a [Match], if two values [first] and [second] match according to the matcher, or
     * null, if the values do not match.
     *
     */
    fun getMatch(first: F, second: S): Match<F, S>?

    /**
     * Returns, whether matches according to the matcher represent a greater similarity, if their
     * heuristic values are greater, false otherwise
     */
    fun isGainMetric(): Boolean

}
