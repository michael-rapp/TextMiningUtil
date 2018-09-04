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

/**
 * A parser that allows to parse text by first splitting it into tokens using a [Tokenizer] and then
 * gradually processing the resulting [TokenSequence] by applying a [Processor] or [ProcessorChain]
 * to it.
 *
 * @param InputTokenType The type of the tokens the text is split up into
 * @param ResultType     The type of the parser's result
 * @author Michael Rapp
 * @since 2.1.0
 */
class GradualTextParser<InputTokenType : Token, ResultType> private constructor(
        private val tokenizer: Tokenizer<InputTokenType>,
        private val processor: Processor<TokenSequence<InputTokenType>, ResultType>) :
        AbstractTextParser<ResultType>() {

    override fun onParse(text: CharSequence): ResultType {
        val tokens = tokenizer.tokenize(text)
        val tokenSequence = TokenSequence.createSorted(tokens)
        return processor.process(tokenSequence)
    }

}
