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

import de.mrapp.textmining.util.tokenizer.Substring
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests the functionality of the class [TokenSequence].
 *
 * @author Michael Rapp
 */
class TokenSequenceTest {

    @Test
    fun testConstructor() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence(tokens)
        val iterator = sequence.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(token1, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(token2, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testCreateSorted() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(token1, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(token2, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(token1, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testCreateSortedWithMapper() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens) { token -> MutableToken(token) }
        val iterator = sequence.iterator()
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
    fun testGetToken() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens) { token -> MutableToken(token) }
        sequence.delimiter = ","
        assertEquals("foo,bar,foo", sequence.getToken())
    }

    @Test
    fun testLength() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens) { token -> MutableToken(token) }
        assertEquals(9, sequence.length)
    }

}
