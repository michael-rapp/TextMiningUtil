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
package de.mrapp.textmining.util.parser

import de.mrapp.textmining.util.Token
import de.mrapp.util.Condition.ensureAtLeast
import de.mrapp.util.Condition.ensureAtMaximum
import de.mrapp.util.Condition.ensureEqual
import de.mrapp.util.Condition.ensureNotEqual
import de.mrapp.util.Condition.ensureNotNull
import de.mrapp.util.Condition.ensureTrue
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList

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
        override val positions: SortedSet<Int> = TreeSet(),
        var delimiter: CharSequence = "") : Iterable<TokenType>, Token {

    companion object {

        /**
         * Creates a new sequence of several [tokens]. The tokens are ordered by their positions
         * (see [Token.positions]). If a token corresponds to multiple positions, it will occur
         * multiple times in the sequence.
         */
        fun <T : Token> createSorted(tokens: Iterable<T>): TokenSequence<T> {
            return createSorted(tokens) { token -> token }
        }

        /**
         * Creates a new sequence from several [tokens] of type [I] that are mapped to a different
         * type [O]. The tokens are ordered by their positions (see [Token.positions]). If a token
         * corresponds to multiple positions, it will occur multiple times in the sequence.
         */
        fun <I : Token, O : Token> createSorted(tokens: Iterable<I>,
                                                mapper: (I) -> O): TokenSequence<O> {
            val sortedMap = TreeMap<Int, O>()
            tokens.forEach {
                it.positions.forEach { position ->
                    sortedMap[position] = mapper.invoke(it)
                }
            }
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
                    TreeSet(sequence.positions), sequence.delimiter)
        }

    }

    /**
     * A [MutableListIterator] that allows to traverse and modify a [TokenSequence].
     *
     * @property tokenSequence     The [TokenSequence] that is traversed by the iterator
     * @property nextIndex         The index of the token in the traversed [TokenSequence], the
     *                             iterator reaches next
     * @property modificationCount The current modification count of the [TokenSequence]. It is used
     *                             to detect concurrent modifications by other iterator instances
     */
    class Iterator<TokenType : Token>(private val tokenSequence: TokenSequence<TokenType>,
                                      private var nextIndex: Int = 0,
                                      private var modificationCount: Long) :
            MutableListIterator<TokenType> {

        private var lastIndex: Int? = null

        init {
            ensureAtLeast(nextIndex, 0, "The next index must be at least 0")
            ensureAtMaximum(nextIndex, tokenSequence.size,
                    "The next index must be at maximum ${tokenSequence.size}")
        }

        /**
         * Merges the current token with the token at a specific [index] using a specific
         * [mergerFunction]. The token at the given [index] will be removed.
         */
        fun merge(index: Int, mergerFunction: (TokenType, TokenType) -> TokenType) {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalArgumentException::class.java)
            ensureNotEqual(lastIndex, index, "Can only merge with different token")
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            val tokenToMerge = tokenSequence.tokens[index]
            val currentIndex = if (lastIndex!! < nextIndex) lastIndex!! else nextIndex
            val tokenToRetain = tokenSequence.tokens[currentIndex]
            val newToken = mergerFunction.invoke(tokenToRetain, tokenToMerge)
            tokenSequence.tokens[currentIndex] = newToken
            tokenSequence.tokens.removeAt(index)

            if (currentIndex > index) {
                lastIndex = lastIndex!! - 1
                nextIndex--
            }

            modificationCount++
            tokenSequence.modificationCount++
        }

        /**
         * Splits the current token into a prefix and a suffix using a specific [dividerFunction].
         * The divider function must return the token, the original token should be split into.
         */
        fun split(dividerFunction: (TokenType) -> Pair<TokenType, TokenType>) {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalArgumentException::class.java)
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            val currentIndex = if (lastIndex!! < nextIndex) lastIndex!! else nextIndex
            val tokenToDivide = tokenSequence.tokens[currentIndex]
            val (existingToken, newToken) = dividerFunction.invoke(tokenToDivide)
            tokenSequence.tokens[currentIndex] = existingToken
            tokenSequence.tokens.add(currentIndex + 1, newToken)
            modificationCount++
            tokenSequence.modificationCount++
        }

        /**
         * Searches for the next token that matches a specific [matcherFunction]. If such a token is
         * found, true will be returned and calling [next] will result in the found token to be
         * returned. If no matching token can be found, false will be returned and the iterator will
         * point to the token that is located after the token, the search looked at last.
         *
         * @param maxSteps The maximum steps to look ahead or -1, if the search should be continued
         *                 until the end of the sequence is reached
         */
        @JvmOverloads
        fun findNext(matcherFunction: (TokenType) -> Boolean, maxSteps: Int = -1): Boolean {
            ensureTrue(maxSteps == -1 || maxSteps > 0,
                    "The maximum number of steps must be -1 or at least 1")
            var steps = 0

            while ((maxSteps == -1 || steps < maxSteps) && hasNext()) {
                steps += 1

                if (matcherFunction.invoke(next())) {
                    previous()
                    return true
                }
            }

            return false
        }

        /**
         * Searches for the last token that matches a specific [matcherFunction]. If such a token is
         * found, true will be returned and calling [previous] will result in the found token to be
         * returned. If no matching token can be found, false will be returned and the iterator will
         * point to the token that is located before the token, the search looked at last.
         *
         * @param maxSteps The maximum steps to look back or -1, if the search should be continued
         *                 until the start of the sequence is reached
         */
        @JvmOverloads
        fun findPrevious(matcherFunction: (TokenType) -> Boolean, maxSteps: Int = -1): Boolean {
            ensureTrue(maxSteps == -1 || maxSteps > 0,
                    "The maximum number of steps must be -1 or at least 1")
            var steps = 0

            while ((maxSteps == -1 || steps < maxSteps) && hasPrevious()) {
                steps += 1

                if (matcherFunction.invoke(previous())) {
                    next()
                    return true
                }
            }

            return false
        }

        override fun hasNext(): Boolean {
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            return nextIndex < tokenSequence.size
        }

        override fun hasPrevious(): Boolean {
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            return nextIndex > 0
        }

        override fun next(): TokenType {
            if (hasNext()) {
                val currentToken = tokenSequence.tokens[nextIndex]
                lastIndex = nextIndex
                nextIndex++
                return currentToken
            }

            throw NoSuchElementException()
        }

        override fun nextIndex(): Int {
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            return if (nextIndex < tokenSequence.size) nextIndex else -1
        }

        override fun previous(): TokenType {
            if (hasPrevious()) {
                lastIndex = nextIndex
                nextIndex--
                return tokenSequence.tokens[nextIndex]
            }

            throw NoSuchElementException()
        }

        override fun previousIndex(): Int {
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            return if (nextIndex > 0) nextIndex - 1 else -1
        }

        override fun add(element: TokenType) {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalStateException::class.java)
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            tokenSequence.tokens.add(lastIndex!!, element)
            modificationCount++
            tokenSequence.modificationCount++
        }

        override fun remove() {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalStateException::class.java)
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            tokenSequence.tokens.removeAt(lastIndex!!)
            nextIndex--
            lastIndex = null
            modificationCount++
            tokenSequence.modificationCount++
        }

        override fun set(element: TokenType) {
            ensureNotNull(lastIndex, "next() or previous() not called",
                    IllegalStateException::class.java)
            ensureEqual(modificationCount, tokenSequence.modificationCount, null,
                    ConcurrentModificationException::class.java)
            tokenSequence.tokens[lastIndex!!] = element
            modificationCount++
            tokenSequence.modificationCount++
        }

    }

    private var modificationCount = 0L

    /**
     * Returns a [MutableListIterator] that allows to traverse and modify the sequence.
     *
     * @param index The index the iterator should start at
     */
    @JvmOverloads
    fun sequenceIterator(index: Int = 0) = TokenSequence.Iterator(this, index, modificationCount)

    /**
     * Returns a list that contains all of the sequence's tokens.
     */
    fun toList(): List<TokenType> = tokens.toList()

    /**
     * The number of tokens that are contained by the sequence.
     */
    val size: Int
        get() = tokens.size

    override val length = tokens.fold(0) { length, token -> length + token.length }

    override var token: CharSequence
        get() = tokens.fold("") { text, token ->
            "$text${if (text.isNotEmpty()) delimiter else ""}${token.token}"
        }
        set(_) {
            throw UnsupportedOperationException()
        }

    override fun addPosition(position: Int) {
        ensureAtLeast(position, 0, "The position must be at least 0")
        this.positions.add(position)
    }

    override fun iterator() = tokens.iterator()

    override fun toString() = token.toString()

}
