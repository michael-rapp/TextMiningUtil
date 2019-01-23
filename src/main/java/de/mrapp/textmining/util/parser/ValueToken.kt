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

import de.mrapp.textmining.util.Token
import de.mrapp.util.Condition.ensureAtLeast
import de.mrapp.util.Condition.ensureEqual
import de.mrapp.util.Condition.ensureNotEmpty
import java.util.*

/**
 * A token that can optionally have a certain value assigned to it.
 *
 * @property token           The token
 * @property value           The value that is associated with the token
 * @property positions       A set that contains the position(s) of the token
 * @author Michael Rapp
 * @since 2.1.0
 */
data class ValueToken<T> @JvmOverloads constructor(
        override var token: CharSequence, var value: T?,
        override val positions: SortedSet<Int> = TreeSet()) : Token {

    init {
        ensureNotEmpty(token, "The token may not be empty")
        ensureEqual(positions.count { it < 0 }, 0, "All positions must be at least 0")
    }

    /**
     * Creates a new token that can optionally have a certain value assigned to it.
     *
     * @param token           The token
     * @param value           The value that is associated with the token
     * @param positions       The position(s) of the token
     */
    constructor(token: CharSequence, value: T?, vararg positions: Int) :
            this(token, value, positions.toList())

    /**
     * Creates a new token that can optionally have a certain value assigned to it.
     *
     * @param token           The token
     * @param value           The value that is associated with the token
     * @param positions       A collection that contains the position(s) of the token
     */
    constructor(token: CharSequence, value: T?, positions: Collection<Int>) :
            this(token, value, TreeSet(positions))

    override fun addPosition(position: Int) {
        ensureAtLeast(position, 0, "The position must be at least 0")
        positions.add(position)
    }

    override fun toString() = token.toString()

}
