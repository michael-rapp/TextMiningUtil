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
package de.mrapp.textmining.util.tokenizer

import de.mrapp.util.Condition.ensureNotEmpty
import de.mrapp.util.Condition.ensureTrue
import java.util.regex.Pattern

/**
 * Allows to split texts into substrings using regular expressions.
 *
 * @property pattern The regular expression that is used to split texts
 * @author Michael Rapp
 * @since 1.2.0
 */
class RegexTokenizer(val pattern: Pattern) : AbstractTokenizer<Substring>() {

    companion object {

        /**
         * Creates a new tokenizer, which allows to split texts at whitespaces.
         */
        fun splitByWhitespace() = RegexTokenizer("\\s+")

        /**
         * Creates a new tokenizer, which allows to split texts at specific [delimiters].
         */
        fun splitByDelimiters(vararg delimiters: String): RegexTokenizer {
            return splitByDelimiters(delimiters.asList())
        }

        /**
         * Creates a new tokenizer, which allows to split texts at specific [delimiters].
         */
        fun splitByDelimiters(delimiters: Iterable<CharSequence>): RegexTokenizer {
            ensureTrue(delimiters.iterator().hasNext(), "At least one delimiter must be given")
            val stringBuilder = StringBuilder()

            for (delimiter in delimiters) {
                ensureNotEmpty(delimiter, "Delimiters may not be empty")

                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.append("|")
                }

                stringBuilder.append(delimiter)
            }

            return RegexTokenizer(stringBuilder.toString())
        }

    }

    /**
     * Creates a new tokenizer, which allows to split texts into substrings using a specific
     * [pattern].
     */
    constructor(pattern: String) : this(Pattern.compile(pattern)) {
        ensureNotEmpty(pattern, "The regular expression may not be empty")
    }

    override fun onTokenize(text: String, map: MutableMap<String, Substring>) {
        var i = 0
        val matcher = pattern.matcher(text)

        while (matcher.find()) {
            val token = text.substring(i, matcher.start())
            addToken(map, token, i) { t, p -> Substring(t, p) }
            i = matcher.end()
        }

        val token = text.substring(i, text.length)
        addToken(map, token, i) { t, p -> Substring(t, p) }
    }

}
