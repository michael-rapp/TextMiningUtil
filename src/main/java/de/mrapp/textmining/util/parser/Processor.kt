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
import de.mrapp.textmining.util.tokenizer.Tokenizer
import sun.nio.cs.CharsetMapping

/**
 * Defines the interface, a processor that is applied to certain data and returns data of another
 * type, must implement.
 *
 * @param I The type of the data to process
 * @param O The type of the resulting data
 * @author Michael Rapp
 * @since 2.1.0
 */
interface Processor<I, O> {

    companion object {

        /**
         * Creates and returns a processor that maps data of type [I] to data of type [O] according
         * to a [mappingFunction].
         */
        fun <I, O> map(mappingFunction: (I) -> O): Processor<I, O> {
            return object : Processor<I, O> {

                override fun process(input: I) = mappingFunction.invoke(input)

            }
        }

        /**
         * Creates and returns a processor that maps a [TokenSequence] of type [I] to a sequence of
         * type [O] according to a [mappingFunction].
         */
        fun <I : Token, O : Token> mapSequence(mappingFunction: (I) -> O):
                Processor<TokenSequence<I>, TokenSequence<O>> {
            return object : Processor<TokenSequence<I>, TokenSequence<O>> {

                override fun process(input: TokenSequence<I>) =
                        TokenSequence.createMapped(input, mappingFunction)

            }
        }

        /**
         * Creates and returns a processor that splits a text into several tokens using a specific
         * [tokenizer].
         */
        fun <TokenType : Token> tokenize(tokenizer: Tokenizer<TokenType>):
                Processor<CharSequence, Collection<TokenType>> {
            return object : Processor<CharSequence, Collection<TokenType>> {

                override fun process(input: CharSequence) = tokenizer.tokenize(input)

            }
        }

    }

    /**
     * Processes a certain [input] and returns the result.
     */
    fun process(input: I): O

}