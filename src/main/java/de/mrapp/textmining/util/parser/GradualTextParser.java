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
package de.mrapp.textmining.util.parser;

import de.mrapp.textmining.util.Token;
import de.mrapp.textmining.util.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * A parser, that allows to parse text by first splitting it into tokens using a {@link Tokenizer}
 * and then gradually processing the resulting {@link TokenSequence} by applying a {@link
 * TokenProcessor} or {@link TokenProcessorChain} to it. Finally, the output of the parser is
 * determined by a {@link TokenMapper}.
 *
 * @param <TokenizedTokenType> The type of the tokens, the text is split into by the parsers
 *                             tokenizer
 * @param <InputTokenType>     The type of the tokens, the that are processed by the parser's
 *                             processor(s)
 * @param <OutputTokenType>    The type of the tokens, that are returned by the parser's
 *                             processor(s)
 * @param <ResultType>         The type of the data, which is returned by the parser's mapper
 * @author Michael Rapp
 * @since 1.3.0
 */
public class GradualTextParser<TokenizedTokenType extends Token, InputTokenType extends Token, OutputTokenType extends Token, ResultType>
        extends AbstractTextParser<ResultType> {

    /**
     * A builder, which allows to configure and create instances of the class {@link
     * GradualTextParser}.
     */
    public static final class Builder {

        /**
         * A builder, which allows to configure and create instances of the class {@link
         * GradualTextParser} once a tokenizer has been set.
         *
         * @param <TokenizedTokenType> The type of the tokens, the text is split into by the
         *                             parser's tokenizer
         * @param <InputTokenType>     The type of the tokens, the that are processed by the
         *                             parser's processor(s)
         */
        public final class TokenizedBuilder<TokenizedTokenType extends Token, InputTokenType extends Token> {

            /**
             * A builder, which allows to configure and create instances of the class {@link
             * GradualTextParser} once a tokenizer and processor(s) has been set.
             *
             * @param <OutputTokenType> The type of the tokens, that are returned by the parser's
             *                          processor(s)
             */
            public final class ProcessedBuilder<OutputTokenType extends Token> {

                /**
                 * A builder, which allows to configure and create instances of the class {@link
                 * GradualTextParser} once a tokenizer, processor(s) and a mapper has been set.
                 *
                 * @param <ResultType> The type of the data, which is returned by the parser's
                 *                     mapper
                 */
                public final class ResultBuilder<ResultType> {

                    /**
                     * The mapper, which is used to map a token sequence to a final result.
                     */
                    private final TokenMapper<OutputTokenType, ResultType> mapper;

                    /**
                     * Creates a new builder, which allows to configure and create instances of the
                     * class {@link  GradualTextParser} once a tokenizer, processor(s) and a mapper
                     * has been set.
                     *
                     * @param mapper The mapper, which should be used to map a token sequence to a
                     *               final result.
                     */
                    ResultBuilder(@NotNull final TokenMapper<OutputTokenType, ResultType> mapper) {
                        this.mapper = mapper;
                    }

                    /**
                     * Creates a new instance of the class {@link GradualTextParser} as configured
                     * by the builder.
                     *
                     * @return The instance, which has been created, as an instance of the class
                     * {@link GradualTextParser}. The instance may not be null
                     */
                    @NotNull
                    public GradualTextParser<TokenizedTokenType, InputTokenType, OutputTokenType, ResultType> build() {
                        return new GradualTextParser<>(tokenizer, mappingFunction, processorChain,
                                mapper);
                    }

                }

                /**
                 * The processor chain, which is used to process a token sequence.
                 */
                private final TokenProcessorChain<InputTokenType, OutputTokenType> processorChain;

                /**
                 * Creates a new builder, which allows to configure and create instances of the
                 * class {@link GradualTextParser} once a tokenizer and processor(s) has been set.
                 *
                 * @param processorChain The processor chain, which should be used to process a
                 *                       token sequence, as an instance of the class {@link
                 *                       TokenProcessorChain}. The processor chain may not be null
                 */
                ProcessedBuilder(
                        @NotNull final TokenProcessorChain<InputTokenType, OutputTokenType> processorChain) {
                    this.processorChain = processorChain;
                }

                /**
                 * Appends an additional processor, which should be used to process the token
                 * sequence, which is made up from the tokens, the original text is split into. The
                 * result of the previous process is passed as the input of the appended processor.
                 *
                 * @param <NewOutputTokenType> The type of the tokens, which are returned by the
                 *                             appended processor
                 * @param processor            The processor, which should be appended, as an
                 *                             instance of the type {@link TokenProcessor}. The
                 *                             processor may not be null
                 * @return The builder, which allows to further configure the {@link
                 * GradualTextParser} to be created, as an instance of the class {@link
                 * ProcessedBuilder}. The builder may not be null
                 */
                @NotNull
                public <NewOutputTokenType extends Token> ProcessedBuilder<NewOutputTokenType> appendProcessor(
                        @NotNull final TokenProcessor<OutputTokenType, NewOutputTokenType> processor) {
                    ensureNotNull(processor, "The processor may not be null");
                    return new ProcessedBuilder<>(processorChain.append(processor));
                }

                /**
                 * Sets the mapper, which should be used to map the processed token sequence to a
                 * final result.
                 *
                 * @param <ResultType> The type of the result, the token sequence is mapped to
                 * @param tokenMapper  The mapper, which should be set, as an instance of the type
                 *                     {@link TokenMapper}. The mapper may not be null
                 * @return The builder, which allows to further configure the {@link
                 * GradualTextParser} to be created, as an instance of the class {@link
                 * ResultBuilder}. The builder may not be null
                 */
                @NotNull
                public <ResultType> ResultBuilder<ResultType> mapResult(
                        @NotNull final TokenMapper<OutputTokenType, ResultType> tokenMapper) {
                    ensureNotNull(tokenMapper, "The mapper may not be null");
                    return new ResultBuilder<>(tokenMapper);
                }

            }

            /**
             * The tokenizer, which is used to split texts into tokens.
             */
            private final Tokenizer<TokenizedTokenType> tokenizer;

            /**
             * The mapping function, which is used to map the original tokens into a token
             * sequence.
             */
            private final Function<TokenizedTokenType, InputTokenType> mappingFunction;

            /**
             * Creates a new builder, which allows to configure and create instances of the class
             * {@link * GradualTextParser} once a tokenizer has been set.
             *
             * @param tokenizer       The tokenizer, which should be used to split texts into
             *                        tokens, as an instance of the type {@link Tokenizer}. The
             *                        tokenizer may not be null
             * @param mappingFunction The mapping function, which should be used to map the original
             *                        tokens into a token sequence, as an instance of the type
             *                        {@link Function}. The mapping function may not be null
             */
            TokenizedBuilder(@NotNull final Tokenizer<TokenizedTokenType> tokenizer,
                             @NotNull final Function<TokenizedTokenType, InputTokenType> mappingFunction) {
                this.tokenizer = tokenizer;
                this.mappingFunction = mappingFunction;
            }

            /**
             * Sets the mapping function, which should be used to map the original tokens into
             * tokens of a different type. The mapped tokens are used to make up a {@link
             * TokenSequence}.
             *
             * @param <MappedTokenType> The type of the tokens, the original tokens should be mapped
             *                          to
             * @param mappingFunction   The mapping function, which should be set, as an instance of
             *                          the type {@link Function}. The mapping function may not be
             *                          null
             * @return The builder, which allows to further configure the {@link GradualTextParser}
             * to be created, as an instance of the class {@link TokenizedBuilder}. The builder may
             * not be null
             */
            @NotNull
            public <MappedTokenType extends Token> TokenizedBuilder<TokenizedTokenType, MappedTokenType> mapTokens(
                    @NotNull final Function<TokenizedTokenType, MappedTokenType> mappingFunction) {
                ensureNotNull(mappingFunction, "The mapping function may not be null");
                return new TokenizedBuilder<>(tokenizer, mappingFunction);
            }

            /**
             * Sets the processor, which should be used to process the token sequence, which is made
             * up from the tokens, the original text is split into.
             *
             * @param <OutputTokenType> The type of the tokens, which are returned by the processor
             * @param processor         The processor, which should be set, as an instance of the
             *                          type {@link TokenProcessor}. The processor may not be null
             * @return The builder, which allows to further configure the {@link GradualTextParser}
             * to be created, as an instance of the class {@link ProcessedBuilder}. The builder may
             * not be null
             */
            @NotNull
            public <OutputTokenType extends Token> ProcessedBuilder<OutputTokenType> setProcessor(
                    @NotNull final TokenProcessor<InputTokenType, OutputTokenType> processor) {
                ensureNotNull(processor, "The processor may not be null");
                return new ProcessedBuilder<>(TokenProcessorChain.create(processor));
            }

        }

        /**
         * Sets the tokenizer, which should be used to split the original text into tokens.
         *
         * @param <TokenType> The type of the tokens, the text is split into
         * @param tokenizer   The tokenizer, which should be set, as an instance of the type {@link
         *                    Tokenizer}. The tokenizer may not be null
         * @return The builder, which allows to further configure the {@link GradualTextParser} to
         * be created, as an instance of the class {@link TokenizedBuilder}. The builder may not be
         * null
         */
        @NotNull
        public <TokenType extends Token> TokenizedBuilder<TokenType, TokenType> tokenize(
                @NotNull final Tokenizer<TokenType> tokenizer) {
            ensureNotNull(tokenizer, "The tokenizer may not be null");
            return new TokenizedBuilder<>(tokenizer, Function.identity());
        }

    }

    /**
     * The tokenizer, which is used to split texts into tokens.
     */
    private final Tokenizer<TokenizedTokenType> tokenizer;

    /**
     * The mapping function, which is used to map the original tokens into a token sequence.
     */
    private final Function<TokenizedTokenType, InputTokenType> mappingFunction;

    /**
     * The processor, which is used to process a token sequence.
     */
    private final TokenProcessor<InputTokenType, OutputTokenType> processor;

    /**
     * The mapper, which is used to map a token sequence to a final result.
     */
    private final TokenMapper<OutputTokenType, ResultType> mapper;

    /**
     * Creates a new gradual text parser.
     *
     * @param tokenizer       The tokenizer, which should be used to split texts into tokens, as an
     *                        instance of the type {@link Tokenizer}. The tokenizer may not be null
     * @param mappingFunction The mapping function, which should be used to map the original tokens
     *                        into a token sequence, as an instance of the type {@link Function}.
     *                        The mapping function may not be null
     * @param processor       The processor, which should be used to process a token sequence, as an
     *                        instance of the type {@link TokenProcessor}. The processor may not be
     *                        null
     * @param mapper          The mapper, which should be used to map a token sequence to a final
     *                        result, as an instance of the type {@link TokenMapper}. The mapper may
     *                        not be null
     */
    protected GradualTextParser(@NotNull final Tokenizer<TokenizedTokenType> tokenizer,
                                @NotNull final Function<TokenizedTokenType, InputTokenType> mappingFunction,
                                @NotNull final TokenProcessor<InputTokenType, OutputTokenType> processor,
                                @NotNull final TokenMapper<OutputTokenType, ResultType> mapper) {
        ensureNotNull(tokenizer, "The tokenizer may not be null");
        ensureNotNull(mappingFunction, "The mapping function may not be null");
        ensureNotNull(processor, "The processor may not be null");
        ensureNotNull(mapper, "The mapper may not be null");
        this.tokenizer = tokenizer;
        this.mappingFunction = mappingFunction;
        this.processor = processor;
        this.mapper = mapper;
    }

    @NotNull
    @Override
    protected final ResultType onParse(@NotNull final String text) throws MalformedTextException {
        Set<TokenizedTokenType> tokens = tokenizer.tokenize(text);
        TokenSequence<InputTokenType> inputTokenSequence = TokenSequence
                .createMappedSequence(tokens, mappingFunction);
        TokenSequence<OutputTokenType> outputTokenSequence = processor.process(inputTokenSequence);
        return mapper.map(outputTokenSequence);
    }

}