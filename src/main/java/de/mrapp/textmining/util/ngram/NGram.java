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
package de.mrapp.textmining.util.ngram;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import static de.mrapp.util.Condition.*;

/**
 * A n-Gram, which consists of a sequence of characters (token), taken from a longer text or word.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class NGram implements Serializable, Cloneable {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = -5907278359683181663L;

    /**
     * The n-gram's token.
     */
    private final String token;

    /**
     * The position of the n-gram's token in the original text or word.
     */
    private final int position;

    /**
     * Creates a new n-gram, which consists of a sequence of characters (token).
     *
     * @param token    The token of the n-gram as a {@link String}. The token may neither be null,
     *                 nor empty
     * @param position The position of the n-gram's token in the original text or word as an {@link
     *                 Integer} value. The position must be at least 0
     */
    public NGram(@NotNull final String token, final int position) {
        ensureNotNull(token, "The token may not be null");
        ensureNotEmpty(token, "The token may not be null");
        ensureAtLeast(position, 0, "The position must be at least 0");
        this.token = token;
        this.position = position;
    }

    /**
     * Returns the token of the n-gram.
     *
     * @return The token of the n-gram as a {@link String}. The token may neither be null, nor empty
     */
    @NotNull
    public final String getToken() {
        return token;
    }

    /**
     * Returns the position of the n-gram's token in the original text or word.
     *
     * @return The position of the n-gram's token as an {@link Integer} value. The position must be
     * at least 0
     */
    public final int getPosition() {
        return position;
    }

    /**
     * Returns the length of the n-gram's token.
     *
     * @return The length of the n-gram's token as an {@link Integer} value
     */
    public final int length() {
        return token.length();
    }

    @Override
    public final NGram clone() {
        return new NGram(token, position);
    }

    @Override
    public final String toString() {
        return "NGram [token=" + token + ", position=" + position + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + token.hashCode();
        result = prime * result + position;
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NGram other = (NGram) obj;
        return token.equals(other.token) && position == other.position;
    }

}
