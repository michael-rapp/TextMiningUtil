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

import java.util.*

/**
 * Defines the interface, a [TextParser] that allows to parse texts that are written in a specific
 * language, must implement.
 *
 * @param ResultType The type of the data, the text should be converted to
 * @author Michael Rapp
 * @since 2.1.0
 */
interface LocalizedTextParser<ResultType> : TextParser<ResultType> {

    /**
     * Defines the interface, a builder that allows to create instances of the type
     * [LocalizedTextParser], must implement.
     *
     * @param T The type of the [LocalizedTextParser] that is created by the factory
     */
    interface Builder<T : LocalizedTextParser<*>> {

        /**
         * Creates a [LocalizedTextParser] that handles a specific language.
         *
         * @param  locale The locale of the language, the [LocalizedTextParser] should handle
         * @throws UnsupportedLocaleException The exception that is thrown, if the specified
         *                                    language is not supported
         */
        @Throws(UnsupportedLocaleException::class)
        fun build(locale: Locale): T

    }

    /**
     * Returns the locale of the language, the parser is able to handle.
     */
    fun getLocale(): Locale

}