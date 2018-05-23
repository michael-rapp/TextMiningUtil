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

import de.mrapp.textmining.util.AbstractToken;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static de.mrapp.util.Condition.ensureNotEmpty;
import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An abstract base class for all tokenizers.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public abstract class AbstractTokenizer<TokenType extends AbstractToken> implements
        Tokenizer<TokenType> {

    /**
     * Adds a new token to a map, if it is not already contained. Otherwise, the token's position is
     * added to the existing token.
     *
     * @param tokens       The map, the token should be added to, as an instance of the type {@link
     *                     Map}. The map may not be null
     * @param token        The token, which should be added, as a {@link String}
     * @param position     The position of the n-gram, which should be created, as an {@link
     *                     Integer} value
     * @param tokenFactory A bi-function, which allows to create a new token, as an instance of the
     *                     type {@link BiFunction}. The bi-function may not be null
     */
    protected final void addToken(@NotNull final Map<String, TokenType> tokens,
                                  @NotNull final String token, final int position,
                                  @NotNull final BiFunction<String, Integer, TokenType> tokenFactory) {
        TokenType existingToken = tokens.get(token);

        if (existingToken == null) {
            existingToken = tokenFactory.apply(token, position);
            tokens.put(token, existingToken);
        } else {
            existingToken.addPosition(position);
        }
    }

    /**
     * The method, which is invoked on subclasses in order to tokenize a specific text.
     *
     * @param text   The text, which should be tokenized, as a {@link String}. The text may neither
     *               be null, nor empty
     * @param tokens A map, the tokens should be added to, as an instance of the type {@link Map}.
     *               The map may not be null
     */
    protected abstract void onTokenize(@NotNull final String text,
                                       @NotNull final Map<String, TokenType> tokens);

    @NotNull
    @Override
    public final Set<TokenType> tokenize(@NotNull final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        Map<String, TokenType> tokens = new HashMap<>();
        onTokenize(text, tokens);
        return new HashSet<>(tokens.values());
    }

}
