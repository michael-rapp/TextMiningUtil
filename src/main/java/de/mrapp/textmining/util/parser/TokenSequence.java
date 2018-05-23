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

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static de.mrapp.util.Condition.ensureNotEmpty;
import static de.mrapp.util.Condition.ensureNotNull;

/**
 * A sequence of several tokens.
 *
 * @param <TokenType> The type of the tokens, the sequence consists of
 * @author Michael Rapp
 * @since 1.3.0
 */
public class TokenSequence<TokenType extends Token> extends AbstractList<TokenType> implements
        Serializable {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 5442528550095948828L;

    /**
     * A list, which contains the tokens, the sequence consists of.
     */
    private final List<TokenType> tokens;

    /**
     * Creates a new sequence of several tokens.
     *
     * @param tokens A list, which contains the tokens, the sequence consists of, as an instance of
     *               the type {@link List}. The list may not be null
     */
    private TokenSequence(@NotNull final List<TokenType> tokens) {
        ensureNotNull(tokens, "The tokens may not be null");
        this.tokens = tokens;
    }

    /**
     * Creates a new sequence of several tokens.
     *
     * @param tokens An iterable, which allows to iterate the tokens, the sequence should consist
     *               of, as an instance of the type {@link Iterable}. The iterable may not be null
     *               and must contain at least one token
     */
    public TokenSequence(@NotNull final Iterable<? extends TokenType> tokens) {
        ensureNotNull(tokens, "The array may not be null");
        ensureNotEmpty(tokens, "At least one token must be provided");
        SortedMap<Integer, TokenType> sortedMap = new TreeMap<>();
        tokens.forEach(
                token -> token.getPositions().forEach(position -> sortedMap.put(position, token)));
        this.tokens = sortedMap.values().stream()
                .collect(() -> new ArrayList<>(sortedMap.size()), List::add, List::addAll);
    }

    /**
     * Creates a new sequence of several tokens, that have been mapped from existing tokens.
     *
     * @param <I>    The type of the existing tokens
     * @param <O>    The type of the tokens, the sequence should consist of
     * @param tokens An iterable, which allows to iterate the tokens, which should be mapped, as an
     *               instance of the type {@link Iterable}. The iterable may not be null
     * @param mapper A function, which allows to map the existing tokens to the tokens, which should
     *               be added to the sequence, as an instance of the type {@link Function}. The
     *               function may not be null
     * @return The sequence, which has been created, as an instance of the class {@link
     * TokenSequence}. The sequence may not be null
     */
    @NotNull
    public static <I extends Token, O extends Token> TokenSequence<O> createMappedSequence(
            @NotNull final Iterable<? extends I> tokens, @NotNull final Function<I, O> mapper) {
        ensureNotNull(mapper, "The mapper may not be null");
        return new TokenSequence<>(StreamSupport.stream(tokens.spliterator(), false).map(mapper)
                .collect(Collectors.toList()));
    }

    @Override
    public final TokenType get(final int index) {
        return tokens.get(index);
    }

    @Override
    public final int size() {
        return tokens.size();
    }

    @Override
    public final TokenSequence<TokenType> clone() {
        return new TokenSequence<>(new ArrayList<>(tokens));
    }

    @Override
    public final String toString() {
        return "TokenSequence{tokens=" + tokens + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tokens.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        if (!super.equals(obj))
            return false;
        TokenSequence<?> other = (TokenSequence<?>) obj;
        return tokens.equals(other.tokens);
    }

}