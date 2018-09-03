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

import de.mrapp.textmining.util.Token
import de.mrapp.util.Condition.ensureAtLeast
import de.mrapp.util.Condition.ensureEqual
import de.mrapp.util.Condition.ensureNotEmpty

/**
 * A n-Gram, which consists of a sequence of characters, taken from a longer text or word.
 *
 * @property n         The degree of the n-gram
 * @property token     The token of the substring
 * @property positions A set that contains the position(s) of the substring's token in the original
 *                     text
 * @author Michael Rapp
 * @since 1.2.0
 */
data class NGram(val n: Int, private val token: String,
                 private val positions: MutableSet<Int> = mutableSetOf()) : Token {

    init {
        ensureAtLeast(n, 1, "The degree must be at least 1")
        ensureNotEmpty(token, "The token may not be empty")
        ensureEqual(positions.count { it < 0 }, 0, "All positions must be at least 0")
    }

    /**
     * Creates a new substring, which consists of a sequence of characters, taken from a longer
     * text.
     *
     * @param n The degree of the n-Gram
     * @param token The token of the substring
     * @param positions An array, which contains the position(s) of the substring's token in the
     * original text
     */
    constructor(n: Int, token: String, vararg positions: Int) :
            this(n, token, positions.toHashSet())

    /**
     * Creates a new substring, which consists of a sequence of characters, taken from a longer
     * text.
     *
     * @param n The degree of the n-Gram
     * @param token The token of the substring
     * @param positions A collection, which contains the position(s) of the substring's token in the
     * original text
     */
    constructor(n: Int, token: String, positions: Collection<Int>) :
            this(n, token, HashSet(positions))

    override fun getToken() = token

    override fun addPosition(position: Int) {
        ensureAtLeast(position, 0, "The position must be at least 0")
        this.positions.add(position)
    }

    override fun getPositions() = positions

    override val length = token.length

    override fun get(index: Int) = token[index]

    override fun subSequence(startIndex: Int, endIndex: Int) =
            token.subSequence(startIndex, endIndex)

}