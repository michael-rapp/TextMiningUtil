/*
 * Copyright 2017 - 2019 Michael Rapp
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
package de.mrapp.textmining.util.parser

import java.io.Serializable

/**
 * A dictionary that contains several entries, which can be used to identify known values and
 * assign them to predefined translations.
 *
 * @param K The type of the values to be translated
 * @param V The type of the translations
 * @author Michael Rapp
 * @since 2.1.0
 */
class Dictionary<K, V> : Iterable<Dictionary.Entry<K, V>>, Serializable {

    /**
     * An entry of a [Dictionary].
     *
     * @property key             The known value to be translated
     * @property value           The translation
     * @property associationType An optional association type
     */
    data class Entry<K, V> @JvmOverloads constructor(
            val key: K, val value: V, val associationType: AssociationType? = null) :
            Serializable

    private val entries: MutableMap<K, Entry<K, V>> = HashMap()

    /**
     * Adds a new [entry] to the dictionary.
     */
    fun addEntry(entry: Entry<K, V>) {
        this.entries[entry.key] = entry
    }

    /**
     * Returns the entry that corresponds to a specific [key].
     */
    fun lookup(key: K) = entries[key]

    /**
     * Returns [Matches] for all entries that match a certain [value] according to a given
     * [matcher].
     */
    fun <T> lookup(value: T, matcher: Matcher<T, K>): Matches<Entry<K, V>, T> {
        return Matches.from(entries.values, value, object : Matcher<Entry<K, V>, T> {

            override fun getMatch(first: Entry<K, V>, second: T) =
                    matcher.getMatch(second, first.key)?.let {
                        Match(first, value, it.heuristicValue)
                    }

            override val isGainMetric = matcher.isGainMetric

        })
    }

    override fun iterator() = entries.values.iterator()

}
