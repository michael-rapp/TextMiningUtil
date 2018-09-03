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
package de.mrapp.textmining.util

import java.io.Serializable

/**
 * Defines the interface, a token, a text can be split into, must implement.
 *
 * @property token The token
 * @property positions A set that contains the position(s) of the token
 */
interface Token : Serializable {

    var token: String

    val positions: MutableCollection<Int>

    /**
     * Adds a new [position].
     */
    fun addPosition(position: Int) {
        positions.add(position)
    }

    /**
     * Returns the length of the token.
     */
    fun length(): Int = token.length

}