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

import de.mrapp.util.Condition.ensureAtLeast
import de.mrapp.util.Condition.ensureTrue

/**
 * Allows to split texts into substrings with a fixed length. E.g. given the length 2, the text
 * "text" is split into the substrings "te", "xt".
 *
 * @property length The length of the substrings that are created by the tokenizer
 * @author Michael Rapp
 * @since 1.2.0
 */
class FixedLengthTokenizer(val length: Int) : AbstractTokenizer<Substring>() {

    init {
        ensureAtLeast(length, 1, "The length must be at least 1")
    }

    override fun onTokenize(text: String, map: MutableMap<String, Substring>) {
        val length = text.length
        ensureTrue(length % this.length == 0,
                "The length of the text must be dividable by ${this.length}")
        var i = 0

        while (i <= length - this.length) {
            val token = text.substring(i, i + this.length)
            addToken(map, token, i) { t, p -> Substring(t, p) }
            i += this.length
        }
    }

}
