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
import de.mrapp.textmining.util.tokenizer.Tokenizer

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

        /**
         * Creates and returns a processor that removes all tokens from a [TokenSequence] that match
         * a given [value] according to a specific [matcher].
         */
        fun <T, TokenType : Token> remove(value: T, matcher: Matcher<TokenType, T>):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return remove { token -> matcher.matches(token, value) }
        }

        /**
         * Creates and returns a processor that removes all tokens from a [TokenSequence] for which
         * a [matcherFunction] returns true.
         */
        fun <TokenType : Token> remove(matcherFunction: (TokenType) -> Boolean):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return object : Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {

                override fun process(input: TokenSequence<TokenType>): TokenSequence<TokenType> {
                    val iterator = input.sequenceIterator()

                    while (iterator.hasNext()) {
                        if (matcherFunction.invoke(iterator.next())) {
                            iterator.remove()
                        }
                    }

                    return input
                }

            }
        }

        /**
         * Creates and returns a processor that retains only those tokens in a [TokenSequence] that
         * match a given [value] according to a specific [matcher].
         */
        fun <T, TokenType : Token> retain(value: T, matcher: Matcher<TokenType, T>):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return retain { token -> matcher.matches(token, value) }
        }

        /**
         * Creates and returns a processor that retains only those tokens in a [TokenSequence] for
         * which a [filterFunction] returns true.
         */
        fun <TokenType : Token> retain(filterFunction: (TokenType) -> Boolean):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return remove { token -> !filterFunction.invoke(token) }
        }

        /**
         * Creates and returns a processor that throws a [MalformedTextException] if not all tokens
         * in a [TokenSequence] fulfill a certain [predicate]
         *
         * @param exceptionFactory Creates and returns the [MalformedTextException] to be thrown
         */
        @JvmOverloads
        fun <TokenType : Token> ensureAllMatch(
                predicate: (TokenType) -> Boolean,
                exceptionFactory: () -> MalformedTextException = { MalformedTextException() }):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return object : Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {

                override fun process(input: TokenSequence<TokenType>): TokenSequence<TokenType> {
                    if (input.iterator().asSequence().all { predicate.invoke(it) }) {
                        return input
                    }

                    throw exceptionFactory.invoke()
                }

            }
        }

        /**
         * Creates and returns a processor that throws a [MalformedTextException] if at least one
         * token in a [TokenSequence] fulfills a certain [predicate]
         *
         * @param exceptionFactory Creates and returns the [MalformedTextException] to be thrown
         */
        @JvmOverloads
        fun <TokenType : Token> ensureNoneMatch(
                predicate: (TokenType) -> Boolean,
                exceptionFactory: () -> MalformedTextException = { MalformedTextException() }):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return object : Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {

                override fun process(input: TokenSequence<TokenType>): TokenSequence<TokenType> {
                    if (input.iterator().asSequence().any { predicate.invoke(it) }) {
                        throw exceptionFactory.invoke()
                    }

                    return input
                }

            }
        }

        /**
         * Creates and returns a processor that throws a [MalformedTextException] if not at least
         * one token in a [TokenSequence] fulfills a certain [predicate]
         *
         * @param exceptionFactory Creates and returns the [MalformedTextException] to be thrown
         */
        @JvmOverloads
        fun <TokenType : Token> ensureAnyMatch(
                predicate: (TokenType) -> Boolean,
                exceptionFactory: () -> MalformedTextException = { MalformedTextException() }):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return object : Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {

                override fun process(input: TokenSequence<TokenType>): TokenSequence<TokenType> {
                    if (input.iterator().asSequence().any { predicate.invoke(it) }) {
                        return input
                    }

                    throw exceptionFactory.invoke()
                }

            }
        }

        /**
         * Creates and returns a processor that applies a specific [processor] to each token in a
         * [TokenSequence].
         */
        fun <TokenType : Token> forEach(processor: Processor<TokenType, *>):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return forEach { token -> processor.process(token) }
        }

        /**
         * Creates and returns a processor that performs a specific [action] for each token in a
         * [TokenSequence].
         */
        fun <TokenType : Token> forEach(action: (TokenType) -> Unit):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return object : Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {

                override fun process(input: TokenSequence<TokenType>): TokenSequence<TokenType> {
                    input.iterator().forEach(action)
                    return input
                }

            }
        }

        /**
         * Creates and returns a processor that applies a specific [ifProcessor] to all tokens of a
         * [TokenSequence] that meet a certain [predicate]. Optionally, a [elseProcessor] is applied
         * to all tokens that do not meet the [predicate].
         */
        @JvmOverloads
        fun <TokenType : Token> conditional(predicate: (TokenType) -> Boolean,
                                            ifProcessor: Processor<TokenType, *>?,
                                            elseProcessor: Processor<TokenType, *>? = null):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return conditional(predicate, { token -> ifProcessor?.process(token) },
                    { token -> elseProcessor?.process(token) })
        }

        /**
         * Creates and returns a processor that applies a specific [ifAction] to all tokens of a
         * [TokenSequence] that meet a certain [predicate]. Optionally, a [elseAction] is applied to
         * all tokens that do not meet the [predicate].
         */
        @JvmOverloads
        fun <TokenType : Token> conditional(predicate: (TokenType) -> Boolean,
                                            ifAction: ((TokenType) -> Unit)?,
                                            elseAction: ((TokenType) -> Unit)? = null):
                Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {
            return object : Processor<TokenSequence<TokenType>, TokenSequence<TokenType>> {

                override fun process(input: TokenSequence<TokenType>): TokenSequence<TokenType> {
                    input.iterator().forEach { i ->
                        if (predicate.invoke(i)) {
                            ifAction?.invoke(i)
                        } else {
                            elseAction?.invoke(i)
                        }
                    }
                    return input
                }

            }
        }

        /**
         * Creates and returns a processor that uses a specific [dictionary] to translate the
         * [MutableToken]s of a [TokenSequence]. The tokens are converted into [ValueToken]s that
         * contain the translation as their values.
         *
         * @param T        The type of the translations
         * @param revision An optional revision number that should be used to mutate the tokens
         */
        @JvmOverloads
        fun <T> translate(dictionary: Dictionary<CharSequence, T>, revision: Int? = null):
                Processor<TokenSequence<MutableToken>, TokenSequence<MutableToken>> {
            return forEach { token ->
                val entry = dictionary.lookup(token.token)
                entry?.let { it ->
                    val valueToken = ValueToken(token.token, it.value, it.associationType,
                            token.positions)
                    revision?.let { token.mutate(token, revision) } ?: token.mutate(valueToken)
                }
            }
        }

        /**
         * Creates and returns a processor that uses a specific [dictionary] and a [matcher] to
         * translate the [MutableToken]s of a [TokenSequence]. The tokens are converted into
         * [ValueToken]s that contain the translation as their values.
         *
         * @param T          The type of the translations
         * @param revision   An optional revision number that should be used to mutate the tokens
         * @param tieBreaker An optional tie breaker that is used, if there are multiple
         *                   translations for a single token
         */
        @JvmOverloads
        fun <T> translate(dictionary: Dictionary<CharSequence, T>,
                          matcher: Matcher<CharSequence, CharSequence>,
                          revision: Int? = null,
                          tieBreaker: TieBreaker<Match<Dictionary.Entry<CharSequence, T>, CharSequence>>? = null):
                Processor<TokenSequence<MutableToken>, TokenSequence<MutableToken>> {
            return forEach { token ->
                val matches = dictionary.lookup(token.token, matcher)
                matches.getBestMatch(tieBreaker)?.let { it ->
                    val entry = it.first
                    val valueToken = ValueToken(token.token, entry.value, entry.associationType,
                            token.positions)
                    revision?.let { token.mutate(valueToken, revision) } ?: token.mutate(valueToken)
                }
            }
        }

    }

    /**
     * Processes a certain [input] and returns the result.
     */
    fun process(input: I): O

}