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
 * Defines the interface, a mapper, which is applied to a {@link TokenSequence} by a {@link
 * GradualTextParser} in order to determine the parser's final output, must implement.
 *
 * @param <TokenType>  The type of the token, the token sequence, the mapper is applied to, consists
 *                     of
 * @param <ResultType> The type of the result, the token sequence is mapped to
 * @author Michael Rapp
 * @since 1.3.0
 */
public interface TokenMapper<TokenType extends Token, ResultType> {

    /**
     * Maps a specific token sequence to a result.
     *
     * @param tokenSequence The token sequence, which should be mapped, as an instance of the class
     *                      {@link TokenSequence}. The token sequence may not be null
     * @return The result, the given token sequence has been mapped to, as an instance of the
     * generic type {@link ResultType}. The result may not be null
     * @throws MalformedTextException The exception, which is thrown, if the given token sequence
     *                                could not be mapped to a result
     */
    @NotNull
    ResultType map(@NotNull TokenSequence<TokenType> tokenSequence) throws MalformedTextException;

}