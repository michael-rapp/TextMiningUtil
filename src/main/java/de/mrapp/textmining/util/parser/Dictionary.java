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
import java.util.stream.Collectors;

import static de.mrapp.util.Condition.ensureNotEmpty;
import static de.mrapp.util.Condition.ensureNotNull;

/**
 * A dictionary, which consists of several entries that can be used to translate identify known
 * tokens and assign predefined values to them.
 *
 * @param <T> The type of the values that are associated with the dictionary's entries
 * @author Michael Rapp
 * @since 1.3.0
 */
public class Dictionary<T> implements Serializable, Iterable<Dictionary.Entry<T>> {

    /**
     * An entry of a {@link Dictionary}.
     *
     * @param <T> The type of the value, which is associated with the entry
     */
    public static class Entry<T> implements Serializable, CharSequence {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -3098943786911307282L;

        /**
         * The text, which corresponds to the entry.
         */
        private final String text;

        /**
         * The association type, which corresponds to the entry.
         */
        private final AssociationType associationType;

        /**
         * The value, which is associated with the entry.
         */
        private final T value;

        /**
         * Creates a new entry.
         *
         * @param text  The text, which corresponds to the entry, as a {@link String}. The text may
         *              neither be null, nor empty
         * @param value The value, which should be associated with the entry, as a value of the
         *              generic type {@link T} or null, if no value should be associated with it
         */
        public Entry(@NotNull final String text, @Nullable final T value) {
            this(text, value, AssociationType.NONE);
        }

        /**
         * Creates a new entry.
         *
         * @param text            The text, which corresponds to the entry, as a {@link String}. The
         *                        text may neither be null, nor empty
         * @param value           The value, which should be associated with the entry, as a value
         *                        of the generic type {@link T} or null, if no value should be
         *                        associated with it
         * @param associationType The association type, which corresponds to the entry, as a value
         *                        of the enum {@link AssociationType}. The association type may not
         *                        be null
         */
        public Entry(@NotNull final String text, @Nullable final T value,
                     @NotNull final AssociationType associationType) {
            ensureNotEmpty(text, "The text may not be empty");
            ensureNotNull(associationType, "The association type may not be null");
            this.text = text;
            this.value = value;
            this.associationType = associationType;
        }

        /**
         * Returns the text, which corresponds to the entry.
         *
         * @return The text, which corresponds to the entry, as a {@link String}. The text may
         * neither be null, nor empty
         */
        @NotNull
        public final String getText() {
            return text;
        }

        /**
         * Returns the value, which is associated with the entry.
         *
         * @return The value, which is associated with the entry, as a value of the generic type
         * {@link T} or null, if no value is associated with it
         */
        public final T getValue() {
            return value;
        }

        /**
         * Returns the association type, which correspons to the entry.
         *
         * @return The association type, which corresponds to the entry, as a value of the enum
         * {@link AssociationType}. The association type may not be null
         */
        @NotNull
        public final AssociationType getAssociationType() {
            return associationType;
        }

        @Override
        public final int length() {
            return text.length();
        }

        @Override
        public final char charAt(final int index) {
            return text.charAt(index);
        }

        @Override
        public final CharSequence subSequence(final int start, final int end) {
            return text.subSequence(start, end);
        }

        @NotNull
        @Override
        public final String toString() {
            return text;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + text.hashCode();
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
            Entry<?> other = (Entry<?>) obj;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return text.equals(other.text) && associationType == other.associationType;
        }

    }

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 210137159904959067L;

    /**
     * The entries, which are contained by the dictionary.
     */
    private final Map<String, Entry<T>> entries = new HashMap<>();

    /**
     * Adds a new entry to the dictionary.
     *
     * @param entry The entry, which should be added, as an instance of the class {@link Entry}. The
     *              entry may not be null
     */
    public final void addEntry(@NotNull final Entry<T> entry) {
        ensureNotNull(entry, "The entry may not be null");
        this.entries.put(entry.getText(), entry);
    }

    /**
     * Returns the entry, which corresponds to a specific text.
     *
     * @param text The text, the entry, which should be returned, corresponds to, as a {@link
     *             String}. The text may neither be null, nor empty
     * @return The entry, which corresponds to the given text, as an instance of the class {@link
     * Entry} or null, if no such entry is available
     */
    public final Entry<T> getEntry(final String text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        return entries.get(text);
    }

    /**
     * Returns all entries of the dictionary, that match a specific token according to a given
     * metric.
     *
     * @param <TokenType> The type of the token, which should be matched
     * @param token       The token, which should be matched, as an instance of the generic type
     *                    {@link TokenType}. The token may not be null
     * @param matcher     The matcher, which should be used to check whether an entry matches the
     *                    given token, as an instance of the type {@link Matcher}. The matcher may
     *                    not be null
     * @return A collection, which contains all entries that match the given token, as an instance
     * of the type {@link Collection} or an empty collection, if no entry matches the token
     */
    @NotNull
    public final <TokenType extends Token> Matches<Entry<T>> getMatchingEntries(
            @NotNull final TokenType token, @NotNull final Matcher<TokenType> matcher) {
        ensureNotNull(token, "The token may not be null");
        ensureNotNull(matcher, "The matcher may not be null");
        return new Matches<>(
                entries.values().stream().map(entry -> matcher.getMatch(token, entry)).filter(
                        Objects::nonNull).collect(Collectors.toList()));
    }

    @NotNull
    @Override
    public final Iterator<Entry<T>> iterator() {
        return entries.values().iterator();
    }

}