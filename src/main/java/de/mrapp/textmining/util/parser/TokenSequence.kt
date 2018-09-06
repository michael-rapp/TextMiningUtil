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

import de.mrapp.textmining.util.Token
import de.mrapp.util.Condition.ensureAtLeast
import de.mrapp.util.Condition.ensureAtMaximum
import de.mrapp.util.Condition.ensureNotNull
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * A sequence of several tokens. As the sequence implements the interface [Token], it can be used as
 * part of a superordinate sequence. In such case, the tokens it consists of are separated using a
 * specific delimiter.
 *
 * @param    TokenType The type of the tokens, the sequence consists of
 * @property tokens    The tokens, the sequence consists of
 * @property positions A set that contains the position(s) of the sequence when part of a
 *                     superordinate sequence
 * @property delimiter The delimiter that is used to separate the sequence's tokens from each other
 * @author Michael Rapp
 * @since 2.1.0
 */
data class TokenSequence<TokenType : Token> @JvmOverloads constructor(
        private val tokens: MutableList<TokenType> = ArrayList(),
        private val positions: MutableSet<Int> = mutableSetOf(),
        var delimiter: CharSequence = "") : Iterable<TokenType>, Token {

    companion object {

        /**
         * Creates a new sequence of several [tokens]. The tokens are ordered by their positions
         * (see [Token.getPositions]). If a token corresponds to multiple positions, it will occur
         * multiple times in the sequence.
         */
        fun <T : Token> createSorted(tokens: Iterable<T>): TokenSequence<T> {
            val sortedMap = TreeMap<Int, T>()
            tokens.forEach { it.getPositions().forEach { position -> sortedMap[position] = it } }
            return TokenSequence(sortedMap.values.toMutableList())
        }

        /**
         * Creates a new sequence from an existing [sequence] of type [I] and maps its tokens to a
         * different type [O].
         */
        fun <I : Token, O : Token> createMapped(sequence: TokenSequence<I>,
                                                mapper: (I) -> O): TokenSequence<O> {
            return TokenSequence(
                    sequence.iterator().asSequence().map { mapper.invoke(it) }.toMutableList(),
                    HashSet(sequence.getPositions()), sequence.delimiter)
        }

    }

    /**
     * A [MutableListIterator] that allows to traverse and modify a [TokenSequence].
     *
     * @property tokenSequence The [TokenSequence] that is traversed by the iterator
     * @property nextIndex     The index of the token in the traversed [TokenSequence], the iterator
     *                         reaches next
     */
    class Iterator<TokenType : Token>(private val tokenSequence: TokenSequence<TokenType>,
                                      private var nextIndex: Int = 0) :
            MutableListIterator<TokenType> {

        private var lastIndex: Int? = null

        init {
            ensureAtLeast(nextIndex, 0, "The next index must be at least 0")
            ensureAtMaximum(nextIndex, tokenSequence.size(),
                    "The next index must be at maximum ${tokenSequence.size()}")
        }

        override fun hasNext() = nextIndex < tokenSequence.size()

        override fun hasPrevious() = nextIndex > 0

        override fun next(): TokenType {
            if (hasNext()) {
                val currentToken = tokenSequence.tokens[nextIndex]
                lastIndex = nextIndex
                nextIndex++
                return currentToken
            }

            throw NoSuchElementException()
        }

        override fun nextIndex() = if (nextIndex < tokenSequence.size()) nextIndex else -1

        override fun previous(): TokenType {
            if (hasPrevious()) {
                lastIndex = nextIndex
                nextIndex--
                return tokenSequence.tokens[nextIndex]
            }

            throw NoSuchElementException()
        }

        override fun previousIndex() = if (nextIndex > 0) nextIndex - 1 else -1

        override fun add(element: TokenType) {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalStateException::class.java)
            tokenSequence.tokens.add(lastIndex!!, element)
        }

        override fun remove() {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalStateException::class.java)
            tokenSequence.tokens.removeAt(lastIndex!!)
            nextIndex--
            lastIndex = null
        }

        override fun set(element: TokenType) {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalStateException::class.java)
            tokenSequence.tokens[lastIndex!!] = element
        }

    }

    /**
     * Returns a [MutableListIterator] that allows to traverse and modify the sequence.
     *
     * @param index The index the iterator should start at
     */
    @JvmOverloads
    fun sequenceIterator(index: Int = 0) = TokenSequence.Iterator(this, index)

    /**
     * The number of tokens that are contained by the sequence.
     */
    fun size() = tokens.size

    override val length = tokens.fold(0) { length, token -> length + token.length }

    override fun getToken() =
            tokens.fold("") { text, token ->
                "$text${if (text.isNotEmpty()) delimiter else ""}${token.getToken()}"
            }

    override fun get(index: Int): Char = getToken()[index]

    override fun addPosition(position: Int) {
        ensureAtLeast(position, 0, "The position must be at least 0")
        this.positions.add(position)
    }

    override fun subSequence(startIndex: Int, endIndex: Int) =
            getToken().subSequence(startIndex, endIndex)

    override fun getPositions() = positions

    override fun iterator() = tokens.iterator()

    override fun toString() = getToken()

}
