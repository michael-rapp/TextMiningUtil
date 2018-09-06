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

/**
 * A mutable token, whose text can be changed without losing the previous revisions.
 *
 * @property token     The current token
 * @property revisions A map that contains all previous tokens mapped to revision numbers
 * @author Michael Rapp
 * @since 2.1.0
 */
data class MutableToken(private var token: Token,
                        private val revisions: MutableMap<Int, Token> = HashMap()) : Token {

    /**
     * Creates a new modifiable token from another token.
     */
    constructor(token: Token) : this(token, HashMap())

    /**
     * Updates the token without specifying a revision number. The current token will be lost.
     */
    fun mutate(token: Token) {
        this.token = token
    }

    /**
     * Updates the token. The current token will be stored using the given [revision] number.
     */
    fun mutate(token: Token, revision: Int) {
        val previousToken = this.token
        mutate(token)
        this.revisions[revision] = previousToken
    }

    /**
     * Returns the current token.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Token> getCurrent() = this.token as T

    /**
     * Returns the token that corresponds to a specific [revision] number.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Token> getRevision(revision: Int) = this.revisions[revision] as? T

    override fun getToken() = this.token.getToken()

    override fun setToken(token: String) {
        this.token.setToken(token)
    }

    override fun addPosition(position: Int) {
        this.token.addPosition(position)
    }

    override fun getPositions() = this.token.getPositions()

    override val length = token.length

    override fun get(index: Int) = token[index]

    override fun subSequence(startIndex: Int, endIndex: Int) =
            token.subSequence(startIndex, endIndex)

    override fun toString() = token.toString()

}
