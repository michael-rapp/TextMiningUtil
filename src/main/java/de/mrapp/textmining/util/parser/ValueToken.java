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

import de.mrapp.textmining.util.AbstractToken;
import de.mrapp.textmining.util.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static de.mrapp.util.Condition.ensureNotNull;

/**
 * A token, which has a certain value assigned to it. Optionally, the token may have an association
 * type assigned, which specifies whether the token is left-associative ({@link
 * AssociationType#LEFT}), right-associative ({@link AssociationType#RIGHT}), or both ({@link
 * AssociationType#BOTH}).
 *
 * @param <T> The type of the assigned value
 * @author Michael Rapp
 * @since 1.3.0
 */
public class ValueToken<T> extends AbstractToken {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 6724529050416698871L;

    /**
     * The value, which is assigned to the token.
     */
    private T value;

    /**
     * The association type of the token.
     */
    private AssociationType associationType;

    /**
     * Creates a new token, which has a certain value assigned to it.
     *
     * @param token     The token as a {@link String}. The token may neither be null, nor empty
     * @param positions A collection, which contains the position(s) of the token in the original
     *                  text, as an instance of the type {@link Collection}. The collection may not
     *                  be null
     * @param value     The value, which should be assigned to the token, as an instance of the
     *                  generic type {@link T} or null, if no value should be assigned to it
     */
    private ValueToken(@NotNull final String token, final Collection<Integer> positions,
                       @Nullable final T value) {
        super(token, positions);
        setValue(value);
        setAssociationType(AssociationType.NONE);
    }

    /**
     * Creates a new token, which has a certain value assigned to it.
     *
     * @param token The token, the token should be created from, as an instance of the type {@link
     *              Token}. The token may not be null
     * @param value The value, which should be assigned to the token, as an instance of the generic
     *              type {@link T} or null, if no value should be assigned to it
     */
    public ValueToken(@NotNull final Token token, @Nullable final T value) {
        this(token.getToken(), token.getPositions(), value);
    }

    /**
     * Returns the value, which is assigned to the token.
     *
     * @return The value, which is assigned to the token, as an instance of the generic type {@link
     * T} or null, if no value is assigned to it
     */
    public final T getValue() {
        return value;
    }

    /**
     * Sets the value, which should be assigned to the token.
     *
     * @param value The value, which should be set, as an instance of the generic type {@link T} or
     *              null, if no value should be set
     */
    public final void setValue(@Nullable final T value) {
        this.value = value;
    }

    /**
     * Returns the association type of the token.
     *
     * @return The association type of the token as a value of the enum {@link AssociationType}. The
     * association type may not be null
     */
    @NotNull
    public final AssociationType getAssociationType() {
        return associationType;
    }

    /**
     * Sets the association type of the token.
     *
     * @param associationType The association type, which should be set, as a value of the enum
     *                        {@link AssociationType}. The association type may not be null
     */
    public final void setAssociationType(@NotNull final AssociationType associationType) {
        ensureNotNull(associationType, "The association type may not be null");
        this.associationType = associationType;
    }

    @Override
    public final ValueToken<T> clone() {
        ValueToken<T> clone = new ValueToken<>(getToken(), getPositions(), value);
        clone.setAssociationType(getAssociationType());
        return clone;
    }

    @Override
    public final String toString() {
        return "ValueToken{token=" + getToken() + ", positions=" + getPositions() + ", value=" +
                value + ", associationType=" + associationType + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (value == null ? 0 : value.hashCode());
        result = prime * result + associationType.hashCode();
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
        ValueToken<?> other = (ValueToken<?>) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return associationType == other.associationType;
    }

}
