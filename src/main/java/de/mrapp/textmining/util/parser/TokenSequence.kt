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
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * A sequence of several tokens.
 *
 * @param TokenType The type of the tokens, the sequence consists of
 * @author Michael Rapp
 * @since 2.1.0
 */
data class TokenSequence<TokenType : Token>(
        private val tokens: MutableList<TokenType> = ArrayList()) :
        AbstractList<TokenType>(), Serializable {

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
         * Creates a new sequence of several [tokens] of type [I] that are mapped to tokens of a
         * different type [O]. The tokens are ordered by their positions (see [Token.getPositions]).
         * If a token corresponds to multiple positions, it will occur multiple times in the
         * sequence.
         */
        fun <I : Token, O : Token> createSorted(tokens: Iterable<I>,
                                                mapper: (I) -> O): TokenSequence<O> {
            return createSorted(tokens.map { mapper.invoke(it) })
        }

    }

    override val size = tokens.size

    override fun get(index: Int) = tokens[index]

}
