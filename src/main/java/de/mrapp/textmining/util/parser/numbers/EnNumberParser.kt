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

    companion object {

        private const val REVISION_TRANSLATE = 0

    }

    private class ResultProcessor : Processor<TokenSequence<MutableToken>, Int> {

        override fun process(input: TokenSequence<MutableToken>): Int {
            // TODO
            return 0
        }

    }

    private val dictionary = Dictionary<CharSequence, Int>()

    private val parser = GradualTextParser.Builder()
            .setTokenizer(RegexTokenizer("\\s+|[^a-z]+"))
            .appendProcessor(Processor.mapSequence<Substring, MutableToken> { token ->
                MutableToken(token)
            })
            .appendProcessor(Processor.translate(dictionary, matcher, REVISION_TRANSLATE))
            .appendProcessor(ResultProcessor())
            .build()

    init {
        dictionary.addEntry(Dictionary.Entry("zero", 0))
        dictionary.addEntry(Dictionary.Entry("null", 0))
        dictionary.addEntry(Dictionary.Entry("nil", 0))
        dictionary.addEntry(Dictionary.Entry("one", 1))
        dictionary.addEntry(Dictionary.Entry("two", 2))
        dictionary.addEntry(Dictionary.Entry("three", 3))
        dictionary.addEntry(Dictionary.Entry("four", 4))
        dictionary.addEntry(Dictionary.Entry("five", 5))
        dictionary.addEntry(Dictionary.Entry("six", 6))
        dictionary.addEntry(Dictionary.Entry("seven", 7))
        dictionary.addEntry(Dictionary.Entry("eight", 8))
        dictionary.addEntry(Dictionary.Entry("nine", 9))
        dictionary.addEntry(Dictionary.Entry("ten", 10))
        dictionary.addEntry(Dictionary.Entry("teen", 10, AssociationType.LEFT))
        dictionary.addEntry(Dictionary.Entry("eleven", 11))
        dictionary.addEntry(Dictionary.Entry("twelve", 12))
        dictionary.addEntry(Dictionary.Entry("thirteen", 13))
        dictionary.addEntry(Dictionary.Entry("fifteen", 15))
        dictionary.addEntry(Dictionary.Entry("eighteen", 18))
        dictionary.addEntry(Dictionary.Entry("twenty", 20, AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("thirty", 30, AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("fourty", 40, AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("fifty", 50, AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("sixty", 60, AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("seventy", 70, AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("eighty", 80, AssociationType.RIGHT))
        dictionary.addEntry(Dictionary.Entry("ninety", 90, AssociationType.RIGHT))
    }

    override fun onParse(text: CharSequence) = parser.parse(text)

    override fun getLocale(): Locale = Locale.ENGLISH

}
