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
package de.mrapp.textmining.util.parser

import de.mrapp.textmining.util.Token
import de.mrapp.util.Condition.ensureAtLeast
import de.mrapp.util.Condition.ensureEqual
import de.mrapp.util.Condition.ensureNotEmpty

/**
 * A token that can optionally have a certain value assigned to it. Additionally, the token may have
 * an association type assigned that specifies whether the token is left-associative
 * [AssociationType.LEFT], right-associative [AssociationType.RIGHT], or both
 * [AssociationType.BIDIRECTIONAL].
 *
 * @property token           The token
 * @property value           The value that is associated with the token
 * @property associationType The association type of the token
 * @property positions       A set that contains the position(s) of the token
 * @author Michael Rapp
 * @since 2.1.0
 */
data class ValueToken<T> @JvmOverloads constructor(
        private var token: String, var value: T?, var associationType: AssociationType?,
        private val positions: MutableSet<Int> = mutableSetOf()) : Token {

    init {
        ensureNotEmpty(token, "The token may not be empty")
        ensureEqual(positions.count { it < 0 }, 0, "All positions must be at least 0")
    }

    /**
     * Creates a new token that can optionally have a certain value assigned to it.
     *
     * @param token           The token
     * @param value           The value that is associated with the token
     * @param associationType The association type of the token
     * @param positions       The position(s) of the token
     */
    constructor(token: String, value: T?, associationType: AssociationType?,
                vararg positions: Int) :
            this(token, value, associationType, positions.toHashSet())

    /**
     * Creates a new token that can optionally have a certain value assigned to it.
     *
     * @param token           The token
     * @param value           The value that is associated with the token
     * @param associationType The association type of the token
     * @param positions       A collection that contains the position(s) of the token
     */
    constructor(token: String, value: T?, associationType: AssociationType?,
                positions: Collection<Int>) :
            this(token, value, associationType, HashSet(positions))

    override fun getToken(): String = token

    override fun setToken(token: String) {
        this.token = token
    }

    override fun addPosition(position: Int) {
        ensureAtLeast(position, 0, "The position must be at least 0")
        positions.add(position)
    }

    override fun getPositions(): Set<Int> = positions

    override val length = token.length

    override fun get(index: Int) = token[index]

    override fun subSequence(startIndex: Int, endIndex: Int) =
            token.subSequence(startIndex, endIndex)

    override fun toString() = token

    override fun copy() = copy(token = token, value = value, associationType = associationType,
            positions = HashSet(positions))

}
