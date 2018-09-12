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
package de.mrapp.textmining.util.tokenizer

import de.mrapp.textmining.util.Token

/**
 * An abstract base class for all tokenizers.
 *
 * @param TokenType The type of the tokens, the texts are split into
 * @author Michael Rapp
 * @since 1.2.0
 */
abstract class AbstractTokenizer<TokenType : Token> : Tokenizer<TokenType> {

    /**
     * Adds a new [token] to a [map], if it is not already contained. Otherwise, the token's
     * [position] is added to the existing token. A [tokenFactory] for converting the given
     * [token] to the type [T] must be provided.
     *
     * @param T The type of the token
     */
    fun <T : Token> addToken(map: MutableMap<String, T>,
                                                      token: String, position: Int,
                                                      tokenFactory: (String, Int) -> T) {
        var existingToken = map[token]

        if (existingToken == null) {
            existingToken = tokenFactory.invoke(token, position)
            map[token] = existingToken
        } else {
            existingToken.addPosition(position)
        }
    }

    /**
     * The method, which is invoked on subclasses in order to tokenize a specific [text]. The tokens
     * should be added to the given [map].
     */
    protected abstract fun onTokenize(text: String,
                                      map: MutableMap<String, TokenType>)

    override fun tokenize(text: CharSequence): Collection<TokenType> {
        if (text.isNotEmpty()) {
            val tokens = HashMap<String, TokenType>()
            onTokenize(text.toString(), tokens)
            return tokens.values
        }

        return emptyList()
    }

}
