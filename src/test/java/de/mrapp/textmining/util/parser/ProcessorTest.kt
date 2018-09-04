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

import de.mrapp.textmining.util.tokenizer.RegexTokenizer
import de.mrapp.textmining.util.tokenizer.Substring
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests the functionality of the class [Processor].
 *
 * @author Michael Rapp
 */
class ProcessorTest {

    @Test
    fun testMap() {
        val processor = Processor.map<Int, String> { input -> input.toString() }
        assertEquals("42", processor.process(42))
    }

    @Test
    fun testMapSequence() {
        val processor = Processor.mapSequence<Substring, MutableToken> { token ->
            MutableToken(token)
        }
        val token1 = Substring("foo", 0, 2)
        val token2 = Substring("bar", 1)
        val sequence = TokenSequence.createSorted(listOf(token1, token2))
        sequence.delimiter = ","
        sequence.addPosition(0)
        val mappedSequence = processor.process(sequence)
        assertEquals(sequence.getPositions(), mappedSequence.getPositions())
        assertEquals(sequence.delimiter, mappedSequence.delimiter)
        val iterator = mappedSequence.iterator()
        assertTrue(iterator.hasNext())
        val mutableToken1 = iterator.next()
        assertEquals(token1.getToken(), mutableToken1.getToken())
        assertTrue(iterator.hasNext())
        val mutableToken2 = iterator.next()
        assertEquals(token2.getToken(), mutableToken2.getToken())
        assertTrue(iterator.hasNext())
        val mutableToken3 = iterator.next()
        assertEquals(token1.getToken(), mutableToken3.getToken())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testTokenize() {
        val processor = Processor.tokenize(RegexTokenizer.splitByWhitespace())
        val tokens = processor.process("foo bar foobar")
        assertEquals(3, tokens.size)
        val tokenStrings = tokens.map { it.getToken() }
        assertTrue(tokenStrings.containsAll(listOf("foo", "bar", "foobar")))
    }

}
