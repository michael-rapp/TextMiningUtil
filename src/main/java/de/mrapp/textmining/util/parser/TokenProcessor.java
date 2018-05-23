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

/**
 * Defines the interface, a processor that is applied to a {@link TokenSequence} by a {@link
 * GradualTextParser}, must implement.
 *
 * @param <InputTokenType>  The type of the tokens that are processed by the processor
 * @param <OutputTokenType> The type of the tokens that returned by the processor
 * @author Michael Rapp
 * @since 1.3.0
 */
public interface TokenProcessor<InputTokenType extends Token, OutputTokenType extends Token> {

    /**
     * Processes a specific token sequence.
     *
     * @param tokenSequence The token sequence, which should be processed, as an instance of the
     *                      class {@link TokenSequence}. The token sequence may not be null
     * @return The processed token sequence as an instance of the class {@link TokenSequence}. The
     * token sequence may not be null
     * @throws MalformedTextException The exception, which is thrown, if the given tokens could not
     *                                be processed
     */
    @NotNull
    TokenSequence<OutputTokenType> process(
            @NotNull TokenSequence<InputTokenType> tokenSequence) throws MalformedTextException;

}