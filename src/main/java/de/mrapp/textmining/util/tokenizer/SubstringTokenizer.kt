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

import de.mrapp.util.Condition.ensureAtLeast

/**
 * Allows to create substrings from texts. E.g. the substrings "t", "e", "x", "t", "te", "ex", "xt",
 * "tex" and "ext" can be created from the text "text".
 *
 * @property minLength The minimum length of the substrings that are created by the tokenizer
 * @property maxLength The maximum length of the substrings that are created by the tokenizer
 * @author Michael Rapp
 * @since 1.0.0
 */
class SubstringTokenizer(val minLength: Int = 1, val maxLength: Int = Int.MAX_VALUE) :
        AbstractTokenizer<Substring>() {

    init {
        ensureAtLeast(minLength, 1, "The minimum length must be at least 1")
        ensureAtLeast(maxLength, minLength,
                "The maximum length must be at least the minimum length")
    }

    override fun onTokenize(text: String, map: MutableMap<String, Substring>) {
        val length = text.length

        for (n in minLength..Math.min(length - 1, maxLength)) {
            for (i in 0..length - n) {
                val token = text.substring(i, i + n)
                addToken(map, token, i) { t, p -> Substring(t, p) }
            }
        }
    }

}
