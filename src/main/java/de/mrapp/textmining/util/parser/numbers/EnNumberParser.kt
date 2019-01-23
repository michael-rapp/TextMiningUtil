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
package de.mrapp.textmining.util.parser.numbers

import de.mrapp.textmining.util.Token
import de.mrapp.textmining.util.parser.*
import de.mrapp.textmining.util.parser.Dictionary
import de.mrapp.textmining.util.tokenizer.DictionaryTokenizer
import de.mrapp.textmining.util.tokenizer.RegexTokenizer
import de.mrapp.textmining.util.tokenizer.Substring
import java.util.*

/**
 * A [NumberParser] that allows to convert textual representation of numbers written in English to
 * integer values.
 *
 * @property matcher The matcher that is used to lookup the textual representations of numbers in a
 *                   dictionary
 * @author Michael Rapp
 * @since 2.1.0
 */
class EnNumberParser(
        private val matcher: Matcher<CharSequence, CharSequence> = Matcher.equals(true)) :
        AbstractTextParser<Int>(), NumberParser {

    private class FlattenProcessor :
            Processor<TokenSequence<MutableToken>, TokenSequence<MutableToken>> {

        override fun process(input: TokenSequence<MutableToken>): TokenSequence<MutableToken> {
            val iterator: TokenSequence.Iterator<MutableToken> = input.sequenceIterator()

            while (iterator.findNext({ token -> token.getCurrent<Token>() is TokenSequence<*> })) {
                val tokenSequence = iterator.next().getCurrent<TokenSequence<MutableToken>>()
                val tokens = tokenSequence.getAllTokens()

                for (i in tokens.size - 1 downTo 0) {
                    if (i == tokens.size - 1) {
                        iterator.set(tokens[i])
                    } else {
                        iterator.add(tokens[i])
                    }
                }
            }

            return input
        }

    }

    private class MergerProcessor :
            Processor<TokenSequence<MutableToken>, TokenSequence<MutableToken>> {

        private fun mergeRight(iterator: TokenSequence.Iterator<MutableToken>,
                               input: TokenSequence<MutableToken>) {
            val nextIndex = iterator.nextIndex()

            if (nextIndex != -1 && nextIndex < input.size) {
                iterator.merge(nextIndex) { tokenToRetain, tokenToMerge ->
                    val first = tokenToRetain.getCurrent<ValueToken<Factor>>().value
                    val second = tokenToMerge.getCurrent<ValueToken<NumericValue<Int>>>()

                    if (second.value !is Number ||
                            second.associationType == AssociationType.RIGHT) {
                        throw MalformedTextException()
                    }

                    val modifiedNumber = first!!.rightOperand!!.apply(first, second.value!!)
                    val newValue = ValueToken(
                            "${tokenToRetain.token}${tokenToMerge.token}", modifiedNumber)

                    if (first.leftOperand != null) {
                        (modifiedNumber as Number).leftOperand = first.leftOperand
                        newValue.associationType = AssociationType.LEFT
                    }

                    tokenToRetain.mutate(newValue)
                    tokenToRetain
                }
            }
        }

        private fun mergeLeft(iterator: TokenSequence.Iterator<MutableToken>,
                              input: TokenSequence<MutableToken>) {
            val previousIndex = iterator.previousIndex()

            if (previousIndex > 0) {
                iterator.merge(iterator.previousIndex() - 1) { tokenToRetain,
                                                               tokenToMerge ->
                    val first = tokenToRetain.getCurrent<ValueToken<Factor>>().value
                    val second = tokenToMerge.getCurrent<ValueToken<NumericValue<Int>>>()

                    if (second.value !is Number ||
                            second.associationType == AssociationType.LEFT) {
                        throw MalformedTextException()
                    }

                    val modifiedNumber = first!!.leftOperand!!.apply(first, second.value!!)
                    val newValue = ValueToken(
                            "${tokenToMerge.token}${tokenToRetain.token}", modifiedNumber)

                    if (first.rightOperand != null) {
                        (modifiedNumber as Number).rightOperand = first.rightOperand
                        newValue.associationType = AssociationType.RIGHT
                    }

                    tokenToRetain.mutate(newValue)
                    tokenToRetain
                }
            }
        }

        override fun process(input: TokenSequence<MutableToken>): TokenSequence<MutableToken> {
            var stop = false

            while (!stop) {
                val iterator: TokenSequence.Iterator<MutableToken> = input.sequenceIterator()
                if (iterator.findNext({ token ->
                            val number = token.getCurrent<ValueToken<NumericValue<Int>>>().value
                                    as? Factor
                            number?.leftOperand != null || number?.rightOperand != null
                        })) {
                    val token = iterator.next().getCurrent<ValueToken<NumericValue<Int>>>()

                    when (token.associationType) {
                        AssociationType.BIDIRECTIONAL -> {
                            mergeLeft(iterator, input)
                            mergeRight(iterator, input)
                        }
                        AssociationType.LEFT -> mergeLeft(iterator, input)
                        AssociationType.RIGHT -> mergeRight(iterator, input)
                        else -> throw IllegalStateException(
                                "Invalid association type ${token.associationType} for numeric value ${token.value!!.javaClass.simpleName}")
                    }
                } else {
                    stop = true
                }
            }

            return input
        }
    }

    private class ResultProcessor : Processor<TokenSequence<MutableToken>, Int> {

        override fun process(input: TokenSequence<MutableToken>): Int {
            val iterator = input.sequenceIterator()

            if (iterator.hasNext()) {
                val token = iterator.next()

                if (!iterator.hasNext()) {
                    val valueToken = token.getCurrent<ValueToken<NumericValue<Int>>>()
                    val numericValue = valueToken.value

                    if (numericValue is Number) {
                        return numericValue.value
                    }
                }
            }

            throw MalformedTextException()
        }

    }

    private val dictionary = Dictionary<CharSequence, NumericValue<Int>>()

    private val filterPredicate: (MutableToken) -> Boolean = { token ->
        ((token.getCurrent() as? ValueToken<*>)?.value as? NumericValue<*>)?.value is Int
    }

    private val parser = GradualTextParser.Builder()
            .setTokenizer(RegexTokenizer("\\s+|[^a-z]+"))
            .appendProcessor(Processor.mapSequence<Substring, MutableToken> { token ->
                MutableToken(token)
            })
            .appendProcessor(Processor.translate(dictionary, matcher))
            .appendProcessor(Processor.conditional(filterPredicate, null, { token ->
                val currentToken = token.getCurrent<Token>().token
                val tokens = DictionaryTokenizer(dictionary).tokenize(currentToken)
                var tokenSequence = TokenSequence.createSorted(tokens) { t -> MutableToken(t) }
                tokenSequence = Processor.translate(dictionary, matcher).process(tokenSequence)
                token.mutate(tokenSequence)
            }))
            .appendProcessor(FlattenProcessor())
            .appendProcessor(Processor.ensureAllMatch(filterPredicate))
            .appendProcessor(MergerProcessor())
            .appendProcessor(ResultProcessor())
            .build()

    init {
        dictionary.addEntry(Dictionary.Entry("zero", Number(0)))
        dictionary.addEntry(Dictionary.Entry("null", Number(0)))
        dictionary.addEntry(Dictionary.Entry("nil", Number(0)))
        dictionary.addEntry(Dictionary.Entry("one", Number(1)))
        dictionary.addEntry(Dictionary.Entry("two", Number(2)))
        dictionary.addEntry(Dictionary.Entry("three", Number(3)))
        dictionary.addEntry(Dictionary.Entry("four", Number(4)))
        dictionary.addEntry(Dictionary.Entry("five", Number(5)))
        dictionary.addEntry(Dictionary.Entry("six", Number(6)))
        dictionary.addEntry(Dictionary.Entry("seven", Number(7)))
        dictionary.addEntry(Dictionary.Entry("eight", Number(8)))
        dictionary.addEntry(Dictionary.Entry("nine", Number(9)))
        dictionary.addEntry(Dictionary.Entry("ten", Number(10)))
        dictionary.addEntry(Dictionary.Entry("teen", Factor(10, leftOperand = Summand()),
                AssociationType.LEFT))
        dictionary.addEntry(Dictionary.Entry("eleven", Number(11)))
        dictionary.addEntry(Dictionary.Entry("twelve", Number(12)))
        dictionary.addEntry(Dictionary.Entry("thirteen", Number(13)))
        dictionary.addEntry(Dictionary.Entry("fifteen", Number(15)))
        dictionary.addEntry(Dictionary.Entry("eighteen", Number(18)))
        dictionary.addEntry(Dictionary.Entry("twenty", Number(20, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("thirty", Number(30, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("forty", Number(40, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("fifty", Number(50, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("sixty", Number(60, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("seventy", Number(70, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("eighty", Number(80, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("ninety", Number(90, rightOperand = Summand()),
                AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("hundred", Number(100, leftOperand = Multiplier(),
                rightOperand = Summand()), AssociationType.BIDIRECTIONAL))
        dictionary.addEntry(Dictionary.Entry("and", Number(0, leftOperand = Summand(),
                rightOperand = Summand()), AssociationType.BIDIRECTIONAL))
    }

    override fun onParse(text: CharSequence) = parser.parse(text)

    override fun getLocale(): Locale = Locale.ENGLISH

}
