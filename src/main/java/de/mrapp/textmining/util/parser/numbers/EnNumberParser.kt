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

import de.mrapp.textmining.util.parser.*
import de.mrapp.textmining.util.parser.Dictionary
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

    private class MergerProcessor :
            Processor<TokenSequence<MutableToken>, TokenSequence<MutableToken>> {

        override fun process(input: TokenSequence<MutableToken>): TokenSequence<MutableToken> {
            val iterator: TokenSequence.Iterator<MutableToken> = input.sequenceIterator()

            while (iterator.findNext({ token -> token.getCurrent<ValueToken<NumericValue<Int>>>().value is Modifier })) {
                val token = iterator.next().getCurrent<ValueToken<NumericValue<Int>>>()

                if (token.associationType == AssociationType.LEFT) {
                    val previousIndex = iterator.previousIndex()

                    if (previousIndex > 0) {
                        iterator.merge(iterator.previousIndex() - 1) { tokenToRetain, tokenToMerge ->
                            val first = tokenToRetain.getCurrent<ValueToken<Modifier<Int>>>()
                            val second = tokenToMerge.getCurrent<ValueToken<NumericValue<Int>>>()
                            val modifiedNumber = first.value!!.apply(second.value!!)
                            tokenToRetain.mutate(
                                    ValueToken("${tokenToMerge.getToken()}${tokenToRetain.getToken()}",
                                            modifiedNumber))
                            tokenToRetain
                        }
                    }
                } else if (token.associationType == AssociationType.RIGHT) {
                    val nextIndex = iterator.nextIndex()

                    if (nextIndex != -1 && nextIndex < input.size()) {
                        iterator.merge(nextIndex) { tokenToRetain, tokenToMerge ->
                            val first = tokenToRetain.getCurrent<ValueToken<Modifier<Int>>>()
                            val second = tokenToMerge.getCurrent<ValueToken<NumericValue<Int>>>()
                            val modifiedNumber = first.value!!.apply(second.value!!)
                            tokenToRetain.mutate(ValueToken(
                                    "${tokenToRetain.getToken()}${tokenToMerge.getToken()}",
                                    modifiedNumber))
                            tokenToRetain
                        }
                    }
                } else {
                    throw IllegalStateException(
                            "Invalid association type ${token.associationType} for numeric value ${token.value!!.javaClass.simpleName}")
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

                    if (numericValue is Number || numericValue is NumberOrSummand) {
                        return numericValue.value
                    }
                }
            }

            throw MalformedTextException()
        }

    }

    private val dictionary = Dictionary<CharSequence, NumericValue<Int>>()

    private val parser = GradualTextParser.Builder()
            .setTokenizer(RegexTokenizer("\\s+|[^a-z]+"))
            .appendProcessor(Processor.mapSequence<Substring, MutableToken> { token ->
                MutableToken(token)
            })
            .appendProcessor(Processor.translate(dictionary, matcher))
            .appendProcessor(Processor.ensureAllMatch<MutableToken>(predicate = { token ->
                ((token.getCurrent() as? ValueToken<*>)?.value as? NumericValue<*>)?.value is Int
            }))
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
        dictionary.addEntry(Dictionary.Entry("teen", Summand(10), AssociationType.LEFT))
        dictionary.addEntry(Dictionary.Entry("eleven", Number(11)))
        dictionary.addEntry(Dictionary.Entry("twelve", Number(12)))
        dictionary.addEntry(Dictionary.Entry("thirteen", Number(13)))
        dictionary.addEntry(Dictionary.Entry("fifteen", Number(15)))
        dictionary.addEntry(Dictionary.Entry("eighteen", Number(18)))
        dictionary.addEntry(Dictionary.Entry("twenty", NumberOrSummand(20), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("thirty", NumberOrSummand(30), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("forty", NumberOrSummand(40), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("fifty", NumberOrSummand(50), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("sixty", NumberOrSummand(60), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("seventy", NumberOrSummand(70), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("eighty", NumberOrSummand(80), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("ninety", NumberOrSummand(90), AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("hundred", NumberOrMultiplier(100), AssociationType.LEFT))
    }

    override fun onParse(text: CharSequence) = parser.parse(text)

    override fun getLocale(): Locale = Locale.ENGLISH

}
