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
 * A parser that allows to parse text by first splitting it into tokens using a [Tokenizer] and then
 * gradually processing the resulting [TokenSequence] by applying a [Processor] or [ProcessorChain]
 * to it.
 *
 * @param    InputTokenType The type of the tokens the text is split up into
 * @param    ResultType     The type of the parser's result
 * @property tokenizer      The tokenizer that is used to split the text into tokens
 * @property processor      The processor or processor chain that is used to process the tokens
 * @author Michael Rapp
 * @since 2.1.0
 */
class GradualTextParser<InputTokenType : Token, ResultType> private constructor(
        private val tokenizer: Tokenizer<InputTokenType>,
        private val processor: Processor<TokenSequence<InputTokenType>, ResultType>) :
        AbstractTextParser<ResultType>() {

    /**
     * A builder that allows to configure and create instances of the class [GradualTextParser].
     */
    class Builder {

        /**
         * A builder that allows to set the first processor that is used by a [GradualTextParser].
         */
        class ProcessorBuilder<InputTokenType : Token>(
                private val tokenizer: Tokenizer<InputTokenType>) {

            /**
             * Adds a new processor that should be used by the parser to process the tokens.
             *
             * @param ResultType The type of the data that is returned by the processor
             */
            fun <ResultType> appendProcessor(
                    processor: Processor<TokenSequence<InputTokenType>, ResultType>) =
                    ProcessorChainBuilder(tokenizer, ProcessorChain.create(processor))

        }

        /**
         * A builder that allows to add additional processors to a [GradualTextParser].
         */
        class ProcessorChainBuilder<InputTokenType : Token, ResultType>(
                private val tokenizer: Tokenizer<InputTokenType>,
                private val processorChain: ProcessorChain<TokenSequence<InputTokenType>, ResultType>) {

            /**
             * Adds a new processor that should be used by the parser to process the tokens.
             *
             * @param NewResultType The type of the data that is returned by the processor
             */
            fun <NewResultType> appendProcessor(processor: Processor<ResultType, NewResultType>) =
                    ProcessorChainBuilder(tokenizer, processorChain.append(processor))

            /**
             * Creates and returns the [GradualTextParser] that was configured by the builder.
             */
            fun build(): GradualTextParser<InputTokenType, ResultType> =
                    GradualTextParser(tokenizer, processorChain)

        }

        /**
         * Sets the [tokenizer] that should be used by the parser to split the text into tokens.
         *
         * @param InputTokenType The type of the tokens, the text should be split into
         */
        fun <InputTokenType : Token> setTokenizer(tokenizer: Tokenizer<InputTokenType>) =
                ProcessorBuilder(tokenizer)

    }

    override fun onParse(text: CharSequence): ResultType {
        val tokens = tokenizer.tokenize(text)
        val tokenSequence = TokenSequence.createSorted(tokens)
        return processor.process(tokenSequence)
    }

}
