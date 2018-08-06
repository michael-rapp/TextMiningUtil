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
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static de.mrapp.util.Condition.*;

/**
 * A sequence of several tokens.
 *
 * @param <TokenType> The type of the tokens, the sequence consists of
 * @author Michael Rapp
 * @since 1.3.0
 */
public class TokenSequence<TokenType extends Token> extends AbstractList<TokenType> implements
        Serializable {

    public static class Iterator<TokenType extends Token> implements ListIterator<TokenType> {

        /**
         * The token sequence, which is traversed by the iterator.
         */
        private final TokenSequence<TokenType> tokenSequence;

        /**
         * The current index of the iterator.
         */
        private int index;

        private TokenType current = null;

        Iterator(@NotNull final TokenSequence<TokenType> tokenSequence) {
            this(tokenSequence, 0);
        }

        Iterator(@NotNull final TokenSequence<TokenType> tokenSequence, final int index) {
            ensureNotNull(tokenSequence, "The token sequence may not be null");
            this.tokenSequence = tokenSequence;
            this.index = index;
        }

        public void merge(final int index) {
            merge(index, null);
        }

        public void merge(final int index, @Nullable final String separator) {
            ensureNotNull(current, "next() or previous() not called",
                    IllegalArgumentException.class);
            ensureNotEqual(this.index, index, "Can only merge with different token");
            TokenType tokenToMerge = tokenSequence.remove(index);
            TokenType tokenToRetain = tokenSequence.get(this.index);
            String newToken =
                    (this.index > index ? tokenToMerge.getToken() : tokenToRetain.getToken()) +
                            (separator != null ? separator : "") +
                            (this.index > index ? tokenToRetain.getToken() :
                                    tokenToMerge.getToken());
            tokenToRetain.setToken(newToken);
            this.index = this.index > index ? this.index - 1 : this.index;
        }

        @SuppressWarnings("unchecked")
        public void split(final Function<String, Integer> dividerFunction) {
            ensureNotNull(current, "next() or previous() not called",
                    IllegalArgumentException.class);
            ensureNotNull(dividerFunction, "The divider function may not be null");
            TokenType tokenToDivide = tokenSequence.get(index);
            int pivot = dividerFunction.apply(tokenToDivide.getToken());
            String prefix = tokenToDivide.getToken().substring(0, pivot);
            String suffix = tokenToDivide.getToken().substring(pivot);
            tokenToDivide.setToken(prefix);
            TokenType newToken = (TokenType) tokenToDivide.clone();
            newToken.setToken(suffix);
            tokenSequence.add(index + 1, newToken);
        }

        @Override
        public boolean hasNext() {
            return index < tokenSequence.size() - 1;
        }

        @Override
        public TokenType next() {
            if (hasNext()) {
                current = tokenSequence.get(index);
                index++;
                return current;
            }

            throw new NoSuchElementException();
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public TokenType previous() {
            if (hasPrevious()) {
                index--;
                current = tokenSequence.get(index);
                return current;
            }

            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return Math.min(tokenSequence.size(), index + 1);
        }

        @Override
        public int previousIndex() {
            return index > 0 ? index - 1 : -1;
        }

        @Override
        public void remove() {
            ensureNotNull(current, "next() or previous() not called",
                    IllegalArgumentException.class);
            tokenSequence.remove(index);
            current = null;
        }

        @Override
        public void set(final TokenType token) {
            ensureNotNull(token, "The token may not be null");
            ensureNotNull(current, "next() or previous() not called",
                    IllegalArgumentException.class);
            tokenSequence.set(index, token);
        }

        @Override
        public void add(final TokenType token) {
            ensureNotNull(token, "The token may not be null");
            ensureNotNull(current, "next() or previous() not called",
                    IllegalArgumentException.class);
            tokenSequence.add(index, token);
        }

    }

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

    /**
     * Creates and returns an iterator, which allows to search for tokens in the sequence. The
     * iterator starts at the first token.
     *
     * @return The iterator, which has been created, as an instance of the class {@link Iterator}.
     * The iterator may not be null
     */
    @NotNull
    public final Iterator<TokenType> sequenceIterator() {
        return sequenceIterator(0);
    }

    /**
     * Creates and returns an iterator, which allows to search for tokens in the sequence.
     *
     * @param index The index of the token, the iterator should start at, as an {@link Integer}
     *              value
     * @return The iterator, which has been created, as an instance of the class {@link Iterator}.
     * The iterator may not be null
     */
    @NotNull
    public final Iterator<TokenType> sequenceIterator(final int index) {
        ensureAtLeast(index, 0, "The index must be at least 0");

        if (!isEmpty()) {
            ensureAtMaximum(index, size() - 1, "The index must be at maximum " + (size() - 1));
        }

        return new Iterator<>(this, index);
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