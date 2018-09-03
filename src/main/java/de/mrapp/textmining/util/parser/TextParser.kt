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

/**
 * Defines the interface, a class that allows to parse text in order to convert it into data of
 * another type, must implement.
 *
 * @param ResultType The type of the data, the text should be converted to
 * @author Michael Rapp
 * @since 2.1.0
 */
interface TextParser<ResultType> {

    /**
     * Sets a [preProcessor] that is applied to a text before it is processed by the parser.
     */
    fun setPreProcessor(preProcessor: ((String) -> String)?)

    /**
     * Sets a [postProcessor] that is applied to the converted data, after it has been processed by
     * the parser.
     */
    fun setPostProcessor(postProcessor: ((ResultType) -> ResultType)?)

    /**
     * Parses a specific [text] in order to convert it into data of another type.
     *
     * @throws MalformedTextException The exception, which is thrown, if the given text is
     * malformed
     */
    @Throws(MalformedTextException::class)
    fun parse(text: String): ResultType

}
