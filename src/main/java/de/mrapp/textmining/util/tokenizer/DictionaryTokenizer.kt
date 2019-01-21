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
package de.mrapp.textmining.util.tokenizer

import de.mrapp.textmining.util.parser.Dictionary

/**
 * @property dictionary
 * @author Michael Rapp
 * @since 2.1.0
 */
class DictionaryTokenizer<K : CharSequence>(val dictionary: Dictionary<K, *>) :
        AbstractTokenizer<Substring>() {

    private data class Match(val start:Int, val end: Int, val token: String)

    override fun onTokenize(text: String, map: MutableMap<String, Substring>) {
        var currentIndex = 0

        while (currentIndex < text.length) {
            val iterator = dictionary.iterator()
            var match: Match? = null

            while (iterator.hasNext() && match == null) {
                val entry = iterator.next()
                val key = entry.key.toString()
                val i = text.indexOf(key, currentIndex)

                if (i != -1) {
                    val token = text.substring(i, i + key.length)
                    match = Match(i, i + key.length, token)
                }
            }

            if (match != null) {
                val token = text.substring(currentIndex, match.start)
                addToken(map, token, currentIndex) { t, p -> Substring(t, p) }
                addToken(map, match.token, match.start) { t, p -> Substring(t, p) }
                currentIndex = match.end
            } else {
                val token = text.substring(currentIndex, text.length)
                addToken(map, token, currentIndex) { t, p -> Substring(t, p) }
                currentIndex = text.length
            }
        }
    }

}
