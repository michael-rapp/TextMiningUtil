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
package de.mrapp.textmining.util.tokenizer

import de.mrapp.textmining.util.metrics.TextMetric
import de.mrapp.textmining.util.parser.Dictionary

/**
 * Allows to split texts based on a [Dictionary]. E.g. given a dictionary that contains the entries
 * "one" and "two", the text "abconedeftwo" is split into the substrings "abc", "one", "def", and
 * "two".
 *
 * @property dictionary The dictionary that is used by the tokenizer
 * @property metric     The metric that is used to compare substrings with keys in the [dictionary]
 *                      or null, if only exact matches should be considered
 * @property threshold  The heuristic value according to the given [metric] that must be reached by
 *                      a substring and a key in the dictionary to be considered equal
 * @author Michael Rapp
 * @since 2.1.0
 */
class DictionaryTokenizer<K : CharSequence> constructor(
        val dictionary: Dictionary<K, *>, val metric: TextMetric?, val threshold: Double) :
        AbstractTokenizer<Substring>() {

    private data class Match(val start: Int, val end: Int, val token: String,
                             val heuristicValue: Double = 1.0)

    /**
     * Creates a tokenizer that allows to split texts based on a [Dictionary]. Only exact matches
     * are considered.
     */
    constructor(dictionary: Dictionary<K, *>) : this(dictionary, null, 0.0)

    private fun findMatch(key: String, text: String, startIndex: Int): Match? {
        return metric?.let { findInexactMatch(key, text, startIndex, it) }
                ?: findExactMatch(key, text, startIndex)
    }

    private fun findExactMatch(key: String, text: String, startIndex: Int): Match? {
        val i = text.indexOf(key, startIndex)

        return if (i != -1) {
            val token = text.substring(i, i + key.length)
            Match(i, i + key.length, token)
        } else null
    }

    private fun findInexactMatch(key: String, text: String, startIndex: Int, metric: TextMetric):
            Match? {
        val tokenizer = SubstringTokenizer()
        var bestMatch: Match? = null

        for (token in tokenizer.tokenize(text.substring(startIndex))) {
            val substring = token.token.toString()
            val h = metric.evaluate(key, substring)

            if (((metric.isGainMetric && h >= threshold) || (metric.isLossMetric && h <= threshold))
                    && (bestMatch == null || ((metric.isGainMetric && h > bestMatch.heuristicValue)
                            || (metric.isLossMetric && h < bestMatch.heuristicValue)))) {

                val start = startIndex + token.positions.first()
                bestMatch = Match(start, start + token.length, substring, h)
            }
        }

        return bestMatch
    }

    override fun onTokenize(text: String, map: MutableMap<String, Substring>) {
        var currentIndex = 0

        while (currentIndex < text.length) {
            val iterator = dictionary.iterator()
            var match: Match? = null

            while (iterator.hasNext()) {
                val entry = iterator.next()
                val key = entry.key.toString()
                val nextMatch = findMatch(key, text, currentIndex)

                if (nextMatch != null && (match == null || match.start > nextMatch.start)) {
                    match = nextMatch
                }
            }

            currentIndex = if (match != null) {
                val token = text.substring(currentIndex, match.start)
                addToken(map, token, currentIndex) { t, p -> Substring(t, p) }
                addToken(map, match.token, match.start) { t, p -> Substring(t, p) }
                match.end
            } else {
                val token = text.substring(currentIndex, text.length)
                addToken(map, token, currentIndex) { t, p -> Substring(t, p) }
                text.length
            }
        }
    }

}
