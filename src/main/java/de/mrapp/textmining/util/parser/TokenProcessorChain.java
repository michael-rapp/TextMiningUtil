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
import org.jetbrains.annotations.NotNull;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * A processor, that executes several processors in a predefined order. The result of the first
 * processor is passed to the second processor and so on.
 *
 * @param <InputTokenType>  The type of the tokens that are processed by the first processor
 * @param <OutputTokenType> The type of the tokens that are returned by the last processor
 * @author Michael Rapp
 * @since 1.3.0
 */
public class TokenProcessorChain<InputTokenType extends Token, OutputTokenType extends Token> implements
        TokenProcessor<InputTokenType, OutputTokenType> {

    /**
     * The first processor of the processor chain.
     */
    private final TokenProcessor<InputTokenType, OutputTokenType> firstProcessor;

    /**
     * Creates a new processor chain.
     *
     * @param processor The first processor of the processor chain, as an instance of the type
     *                  {@link TokenProcessor}. The processor may not be null
     */
    private TokenProcessorChain(
            @NotNull final TokenProcessor<InputTokenType, OutputTokenType> processor) {
        ensureNotNull(processor, "The processor may not be null");
        this.firstProcessor = processor;
    }

    /**
     * Creates a new processor chain, consisting of a single processor.
     *
     * @param processor         The processor of the processor chain as an instance of the type
     *                          {@link TokenProcessor}. The processor may not be null
     * @param <InputTokenType>  The type of the tokens that are processed by the given processor
     * @param <OutputTokenType> The type of the tokens that are returned by the given processor
     * @return The processor chain, which has been created, as an instance of the class {@link
     * TokenProcessorChain}. The processor chain may not be null
     */
    @NotNull
    public static <InputTokenType extends Token, OutputTokenType extends Token> TokenProcessorChain<InputTokenType, OutputTokenType> create(
            @NotNull final TokenProcessor<InputTokenType, OutputTokenType> processor) {
        return new TokenProcessorChain<>(processor);
    }

    /**
     * Appends a new processor to the processor chain.
     *
     * @param processor            The processor, which should be appended, as an instance of the
     *                             type {@link TokenProcessor}. The processor may not be null
     * @param <NewOutputTokenType> The type of the tokens that are returned by the appended
     *                             processor
     * @return The modified processor chain as an instance of the class {@link TokenProcessorChain}.
     * The processor chain may not be null
     */
    @NotNull
    public <NewOutputTokenType extends Token> TokenProcessorChain<InputTokenType, NewOutputTokenType> append(
            @NotNull final TokenProcessor<OutputTokenType, NewOutputTokenType> processor) {
        ensureNotNull(processor, "The processor may not be null");
        return new TokenProcessorChain<>(
                data -> processor.process(TokenProcessorChain.this.firstProcessor.process(data)));
    }

    @NotNull
    @Override
    public final TokenSequence<OutputTokenType> process(
            @NotNull final TokenSequence<InputTokenType> tokenSequence) throws
            MalformedTextException {
        return firstProcessor.process(tokenSequence);
    }

}