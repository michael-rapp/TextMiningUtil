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

import java.io.Serializable

/**
 * Contains various matches and provides methods to select the best one among them.
 *
 * @param    T            The type of the matches' values
 * @property matches      An iterable that contains the matches
 * @property isGainMetric True, if matches with greater heuristic values are considered to be
 *                        better, false otherwise
 * @author Michael Rapp
 * @since 2.1.0
 */
class Matches<T>(private val matches: Iterable<Match<T>>, val isGainMetric: Boolean) :
        Iterable<Match<T>>, Serializable {

    private fun getBestMatch(match1: Match<T>, match2: Match<T>,
                             tieBreaker: ((Match<T>, Match<T>) -> Match<T>)?): Match<T> {
        val heuristicValue1 = match1.heuristicValue
        val heuristicValue2 = match2.heuristicValue

        if (heuristicValue1 == heuristicValue2) {
            return tieBreaker?.invoke(match1, match2) ?: match1
        } else if (isGainMetric) {
            return if (heuristicValue1 > heuristicValue2) match1 else match2
        } else {
            return if (heuristicValue1 < heuristicValue2) match1 else match2
        }
    }

    /**
     * Returns the best match according to the matches' heuristic values. Optionally, a [tieBreaker]
     * can be specified. If two matches have the same heuristic value, it is used to decide which
     * one of them is considered to be better.
     */
    @JvmOverloads
    fun getBestMatch(tieBreaker: ((Match<T>, Match<T>) -> Match<T>)? = null): Match<T>? {
        val iterator = iterator()

        if (iterator.hasNext()) {
            return iterator.asSequence().reduce { acc, match ->
                getBestMatch(acc, match, tieBreaker)
            }
        }

        return null
    }

    override fun iterator() = matches.iterator()

}
