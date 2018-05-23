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
package de.mrapp.textmining.util.tokenizer;

import de.mrapp.textmining.util.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Defines the interface, a class, which allows to split texts into tokens, must implement.
 *
 * @param <TokenType> The type of the tokens, the texts are split into
 * @author Michael Rapp
 * @since 1.2.0
 */
public interface Tokenizer<TokenType extends Token> {

    /**
     * Splits a specific text into tokens.
     *
     * @param text The text, which should be split into tokens, as a {@link String}. The text may
     *             neither be null, nor empty
     * @return A set, which contains the tokens, the given text has been split into, as an instance
     * of the type {@link Set}. The set may neither be null, nor empty
     */
    @NotNull
    Set<TokenType> tokenize(@NotNull String text);

}
