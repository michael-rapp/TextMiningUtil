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
 * An abstract base class for all parsers that allow to parse text in order to convert it into data
 * of another type.
 *
 * @param ResultType The type of the data, the text should be converted to
 * @author Michael Rapp
 * @since 2.1.0
 */
abstract class AbstractTextParser<ResultType> : TextParser<ResultType> {

    private var preProcessor: ((CharSequence) -> CharSequence)? = null

    private var postProcessor: ((ResultType) -> ResultType)? = null

    private fun preProcess(text: CharSequence): CharSequence = preProcessor?.invoke(text) ?: text

    private fun postProcess(result: ResultType): ResultType =
            postProcessor?.invoke(result) ?: result

    /**
     * The method that is invoked on implementing subclasses in order to parse a specific
     * (eventually pre-processed) [text].
     */
    @Throws(MalformedTextException::class)
    protected abstract fun onParse(text: CharSequence): ResultType

    override fun setPreProcessor(preProcessor: ((CharSequence) -> CharSequence)?) {
        this.preProcessor = preProcessor
    }

    override fun setPostProcessor(postProcessor: ((ResultType) -> ResultType)?) {
        this.postProcessor = postProcessor
    }

    override fun parse(text: CharSequence): ResultType {
        val preProcessedText = preProcess(text)
        val result = onParse(preProcessedText)
        return postProcess(result)
    }

}
