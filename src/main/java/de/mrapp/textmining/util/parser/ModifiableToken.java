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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * A modifiable token. The text of the token can be changed without losing the previous revisions.
 *
 * @author Michael Rapp
 * @since 1.3.0
 */
public class ModifiableToken implements Token {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 6225749001382066967L;

    /**
     * The current token.
     */
    private Token token;

    /**
     * A map, which contains all previous tokens.
     */
    private Map<Integer, Token> revisions = new HashMap<>();

    /**
     * Creates a new modifiable token.
     *
     * @param token The token, the modifiable token should be created from, as an instance of the
     *              type {@link Token}. The token may not be null
     */
    public ModifiableToken(@NotNull final Token token) {
        ensureNotNull(token, "The token may not be null");
        this.token = token;
    }

    /**
     * Sets the token. Without specifying a revision, the current token will be lost.
     *
     * @param token The token, which should be set, as a {@link String}. The token may neither be
     *              null, nor empty
     */
    public final void setToken(@NotNull final Token token) {
        ensureNotNull(token, "The token may not be null");
        this.token = token;
    }

    /**
     * Sets the token. The current token will be stored using the given revision.
     *
     * @param token    The token, which should be set, as an instance of the type {@link Token}. The
     *                 token may not be null
     * @param revision The revision, which should be used to store the current token, as an {@link
     *                 Integer} value
     */
    public final void setToken(@NotNull final Token token, final int revision) {
        Token previousToken = this.token;
        setToken(token);
        revisions.put(revision, previousToken);
    }


    /**
     * Returns the current revision of the token.
     *
     * @param <T> The type, the returned token should be implicitly casted to
     * @return The token, which corresponds to the current revision, casted to the generic type
     * {@link T}. The token may not be null
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public final <T extends Token> T getCurrent() {
        return (T) token;
    }

    /**
     * Returns a previous revision of the token.
     *
     * @param <T>      The type, the returned token should be implicitly casted to
     * @param revision The revision, which should be returned, as an {@link Integer} value
     * @return The token, which corresponds to the given revision, casted to the generic type {@link
     * T} or null, if the given revision is not available
     */
    @SuppressWarnings("unchecked")
    public final <T extends Token> T getRevision(final int revision) {
        return (T) revisions.get(revision);
    }

    @NotNull
    @Override
    public final String getToken() {
        return token.getToken();
    }

    @NotNull
    @Override
    public final Set<Integer> getPositions() {
        return token.getPositions();
    }

    @Override
    public final ModifiableToken clone() {
        ModifiableToken clone = new ModifiableToken(token);
        clone.revisions = new HashMap<>(revisions);
        return clone;
    }

    @Override
    public final String toString() {
        return "ModifiableToken{token=" + getToken() + ", positions=" + getPositions() + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + revisions.hashCode();
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
        ModifiableToken other = (ModifiableToken) obj;
        return revisions.equals(other.revisions);
    }

}