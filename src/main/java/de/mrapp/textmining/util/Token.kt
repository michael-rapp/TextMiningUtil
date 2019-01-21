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
package de.mrapp.textmining.util

import java.io.Serializable

/**
 * Defines the interface, a token, a text can be split into, must implement.
 */
interface Token : CharSequence, Serializable {

    /**
     * The token as a [CharSequence].
     */
    var token: CharSequence

    /**
     * A set that contains the positions the token occurs at.
     */
    val positions: Set<Int>

    /**
     * Adds a new [position] the token occurs at.
     */
    fun addPosition(position: Int)

    override val length: Int
        get() = token.length

    override fun get(index: Int) = token[index]

    override fun subSequence(startIndex: Int, endIndex: Int) =
            token.subSequence(startIndex, endIndex)

}
