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
import kotlin.test.*

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
    fun testCreateMapped() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        sequence.addPosition(0)
        sequence.delimiter = ","
        val mappedSequence = TokenSequence.createMapped(sequence) { token -> MutableToken(token) }
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
    fun testGetToken() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        sequence.delimiter = ","
        assertEquals("foo,bar,foo", sequence.getToken())
    }

    @Test
    fun testLength() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        assertEquals(9, sequence.length)
    }

    @Test
    fun testSequenceIterator() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()
        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }
    }

    @Test
    fun testSequenceIteratorReverse() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator(sequence.size())
        assertTrue(iterator.hasPrevious())
        assertEquals(2, iterator.previousIndex())
        assertEquals("foo", iterator.previous().getToken())
        assertTrue(iterator.hasPrevious())
        assertEquals(1, iterator.previousIndex())
        assertEquals("bar", iterator.previous().getToken())
        assertTrue(iterator.hasPrevious())
        assertEquals(0, iterator.previousIndex())
        assertEquals("foo", iterator.previous().getToken())
        assertFalse(iterator.hasPrevious())
        assertEquals(-1, iterator.previousIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.previous() }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSequenceIteratorThrowsExceptionIfIndexIsLessThanZero() {
        val sequence = TokenSequence<Substring>()
        sequence.sequenceIterator(-1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSequenceIteratorThrowsExceptionIfIndexIsGreaterThanSize() {
        val sequence = TokenSequence<Substring>()
        sequence.sequenceIterator(1)
    }

    @Test(expected = IllegalStateException::class)
    fun testSequenceIteratorSetThrowsException() {
        val sequence = TokenSequence<Substring>()
        val iterator = sequence.sequenceIterator()
        iterator.set(Substring("foo"))
    }

    @Test
    fun testSequenceIteratorSet() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()

        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().getToken())
        iterator.set(Substring("modified"))
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().getToken())
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("modified", iterator2.next().getToken())
        assertTrue(iterator2.hasNext())
        assertEquals(2, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().getToken())
        assertFalse(iterator2.hasNext())
        assertEquals(-1, iterator2.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator2.next() }
    }

    @Test(expected = IllegalStateException::class)
    fun testSequenceIteratorAddThrowsException() {
        val sequence = TokenSequence<Substring>()
        val iterator = sequence.sequenceIterator()
        iterator.add(Substring("foo"))
    }

    @Test
    fun testSequenceIteratorAdd() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()

        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().getToken())
        iterator.add(Substring("added"))
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("bar", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().getToken())
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("added", iterator2.next().getToken())
        assertTrue(iterator2.hasNext())
        assertEquals(2, iterator2.nextIndex())
        assertEquals("bar", iterator2.next().getToken())
        assertTrue(iterator2.hasNext())
        assertEquals(3, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().getToken())
        assertFalse(iterator2.hasNext())
        assertEquals(-1, iterator2.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator2.next() }
    }

    @Test(expected = IllegalStateException::class)
    fun testSequenceIteratorRemoveThrowsException() {
        val sequence = TokenSequence<Substring>()
        val iterator = sequence.sequenceIterator()
        iterator.remove()
    }

    @Test
    fun testSequenceIteratorRemove() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()

        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().getToken())
        iterator.remove()
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("foo", iterator.next().getToken())
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().getToken())
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().getToken())
        assertFalse(iterator2.hasNext())
        assertEquals(-1, iterator2.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator2.next() }
    }

}
