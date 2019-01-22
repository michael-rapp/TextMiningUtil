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

import de.mrapp.textmining.util.metrics.LevenshteinDistance
import de.mrapp.textmining.util.parser.Dictionary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
        assertNull(dictionaryTokenizer.metric)
        assertEquals(0.0, dictionaryTokenizer.threshold)
    }

    @Test
    fun testConstructorWithMetricArgument() {
        val dictionary = Dictionary<String, String>()
        val metric = LevenshteinDistance()
        val threshold = 0.5
        val dictionaryTokenizer = DictionaryTokenizer(dictionary, metric, threshold)
        assertEquals(dictionary, dictionaryTokenizer.dictionary)
        assertEquals(metric, dictionaryTokenizer.metric)
        assertEquals(threshold, dictionaryTokenizer.threshold)
    }

    @Test
    fun testTokenize() {
        val dictionary = Dictionary<String, String>()
        dictionary.addEntry(Dictionary.Entry("foo", "foo2"))
        dictionary.addEntry(Dictionary.Entry("bar", "bar2"))
        val dictionaryTokenizer = DictionaryTokenizer(dictionary)
        val tokens = dictionaryTokenizer.tokenize("xxxfooyyybarzzz")
        assertEquals(5, tokens.size)
        assertTrue(tokens.contains(Substring("xxx", 0)))
        assertTrue(tokens.contains(Substring("foo", 3)))
        assertTrue(tokens.contains(Substring("yyy", 6)))
        assertTrue(tokens.contains(Substring("bar", 9)))
        assertTrue(tokens.contains(Substring("zzz", 12)))
    }

    @Test
    fun testTokenizeIfEntireTextIsInDictionary() {
        val dictionary = Dictionary<String, String>()
        dictionary.addEntry(Dictionary.Entry("foo", "foo2"))
        val dictionaryTokenizer = DictionaryTokenizer(dictionary)
        val tokens = dictionaryTokenizer.tokenize("foo")
        assertEquals(1, tokens.size)
        assertTrue(tokens.contains(Substring("foo", 0)))
    }

    @Test
    fun testTokenizeIfSubstringsAreInDictionary() {
        val dictionary = Dictionary<String, String>()
        dictionary.addEntry(Dictionary.Entry("foo", "foo2"))
        dictionary.addEntry(Dictionary.Entry("foobar", "foobar2"))
        dictionary.addEntry(Dictionary.Entry("bar", "bar2"))
        val dictionaryTokenizer = DictionaryTokenizer(dictionary)
        val tokens = dictionaryTokenizer.tokenize("foobarxxx")
        assertEquals(2, tokens.size)
        assertTrue(tokens.contains(Substring("foobar", 0)))
        assertTrue(tokens.contains(Substring("xxx", 6)))
    }

    @Test
    fun testTokenizeUsingMetric() {
        val dictionary = Dictionary<String, String>()
        dictionary.addEntry(Dictionary.Entry("foo", "foo2"))
        dictionary.addEntry(Dictionary.Entry("bar", "bar2"))
        val dictionaryTokenizer = DictionaryTokenizer(dictionary, LevenshteinDistance(), 1.0)
        val tokens = dictionaryTokenizer.tokenize("xxxfooyyybalzzz")
        assertEquals(5, tokens.size)
        assertTrue(tokens.contains(Substring("xxx", 0)))
        assertTrue(tokens.contains(Substring("foo", 3)))
        assertTrue(tokens.contains(Substring("yyy", 6)))
        assertTrue(tokens.contains(Substring("bal", 9)))
        assertTrue(tokens.contains(Substring("zzz", 12)))
    }

}
