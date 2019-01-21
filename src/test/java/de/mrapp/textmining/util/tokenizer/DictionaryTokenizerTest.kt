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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests the functionality of the class [DictionaryTokenizer].
 *
 * @author Michael Rapp
 */
class DictionaryTokenizerTest {

    @Test
    fun testConstructor() {
        val dictionary = Dictionary<String, String>()
        val dictionaryTokenizer = DictionaryTokenizer(dictionary)
        assertEquals(dictionary, dictionaryTokenizer.dictionary)
    }

    @Test
    fun testTokenize() {
        val dictionary = Dictionary<String, String>()
        dictionary.addEntry(Dictionary.Entry("foo", "bar"))
        val dictionaryTokenizer = DictionaryTokenizer(dictionary)
        val tokens = dictionaryTokenizer.tokenize("xxxfooyyy")
        assertEquals(3, tokens.size)
        assertTrue(tokens.contains(Substring("xxx", 0)))
        assertTrue(tokens.contains(Substring("foo", 3)))
        assertTrue(tokens.contains(Substring("yyy", 6)))
    }

}
