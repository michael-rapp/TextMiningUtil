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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

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
    private final Collection<Entry<T>> entries = new LinkedList<>();

    /**
     * Adds a new entry to the dictionary.
     *
     * @param entry The entry, which should be added, as an instance of the class {@link Entry}. The
     *              entry may not be null
     */
    public final void addEntry(@NotNull final Entry<T> entry) {
        ensureNotNull(entry, "The entry may not be null");
        this.entries.add(entry);
    }

    @NotNull
    @Override
    public final Iterator<Entry<T>> iterator() {
        return entries.iterator();
    }

}