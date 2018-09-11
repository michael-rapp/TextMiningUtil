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

import de.mrapp.textmining.util.parser.LocalizedTextParser
import de.mrapp.textmining.util.parser.TextParser
import de.mrapp.textmining.util.parser.UnsupportedLocaleException
import java.util.*

/**
 * Defines the interface, a [TextParser] that allows to convert textual representations of numbers
 * into integer values, must implement.
 *
 * @author Michael Rapp
 * @since 2.1.0
 */
interface NumberParser : LocalizedTextParser<Int> {

    /**
     * A factory the allows to create instances of the class [NumberParser].
     */
    class Factory : LocalizedTextParser.Factory<NumberParser> {

        private val parsers = listOf(EnNumberParser())

        override fun create(locale: Locale): NumberParser {
            parsers.find { it.getLocale() == locale }?.let { return it }
                    ?: throw UnsupportedLocaleException(
                            "The locale $locale is currently not supported by the parser ${NumberParser::class.java.name}")
        }

    }

}
