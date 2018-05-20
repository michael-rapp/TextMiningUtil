/*
 * Copyright 2017 Michael Rapp
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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static de.mrapp.util.Condition.*;

/**
 * An abstract base class for all tokens.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public abstract class AbstractToken implements Tokenizer.Token {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 2165246123801961089L;

    /**
     * The token.
     */
    private final String token;

    /**
     * The position(s) of the token in the original text.
     */
    private final Set<Integer> positions;

    /**
     * Creates a new token, which consists of a sequence of characters.
     *
     * @param token     The token as a {@link String}. The token may neither be null, nor empty
     * @param positions A collection, which contains the position(s) of the token in the original
     *                  text, as an instance of the type {@link Collection}. The collection may not
     *                  be null
     */
    public AbstractToken(@NotNull final String token,
                         @NotNull final Collection<Integer> positions) {
        ensureNotNull(token, "The token may not be null");
        ensureNotEmpty(token, "The token may not be null");
        ensureNotNull(positions, "The collection may not be null");
        this.token = token;
        this.positions = new HashSet<>();
        this.positions.addAll(positions);
    }

    /**
     * Creates a new token, which consists of a sequence of characters.
     *
     * @param token     The token as a {@link String}. The token may neither be null, nor empty
     * @param positions An array, which contains the position(s) of the token in the original text
     *                  as an {@link Integer} array. The array may neither be null, nor empty
     */
    public AbstractToken(@NotNull final String token, @NotNull final int... positions) {
        this(token, Collections.emptyList());
        ensureAtLeast(positions.length, 1, "The array must contain at least one position");

        for (int position : positions) {
            addPosition(position);
        }
    }

    /**
     * Adds a new position of the token in the original text.
     *
     * @param position The position, which should be added, as an {@link Integer} value. The
     *                 position must be at least 0
     */
    public void addPosition(final int position) {
        ensureAtLeast(position, 0, "The position must be at least 0");
        this.positions.add(position);
    }

    @Override
    public Set<Integer> getPositions() {
        return Collections.unmodifiableSet(positions);
    }

    @NotNull
    @Override
    public String getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + token.hashCode();
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
        AbstractToken other = (AbstractToken) obj;
        return token.equals(other.token);
    }

}
