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
package de.mrapp.textmining.util.parser

import de.mrapp.textmining.util.tokenizer.Substring
import java.util.*
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
        assertEquals(sequence.positions, mappedSequence.positions)
        assertEquals(sequence.delimiter, mappedSequence.delimiter)
        val iterator = mappedSequence.iterator()
        assertTrue(iterator.hasNext())
        val mutableToken1 = iterator.next()
        assertEquals(token1.token, mutableToken1.token)
        assertTrue(iterator.hasNext())
        val mutableToken2 = iterator.next()
        assertEquals(token2.token, mutableToken2.token)
        assertTrue(iterator.hasNext())
        val mutableToken3 = iterator.next()
        assertEquals(token1.token, mutableToken3.token)
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testGetToken() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        sequence.delimiter = ","
        assertEquals("foo,bar,foo", sequence.token)
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
        assertEquals("foo", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("foo", iterator.next().token)
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
        val iterator = sequence.sequenceIterator(sequence.size)
        assertTrue(iterator.hasPrevious())
        assertEquals(2, iterator.previousIndex())
        assertEquals("foo", iterator.previous().token)
        assertTrue(iterator.hasPrevious())
        assertEquals(1, iterator.previousIndex())
        assertEquals("bar", iterator.previous().token)
        assertTrue(iterator.hasPrevious())
        assertEquals(0, iterator.previousIndex())
        assertEquals("foo", iterator.previous().token)
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
        assertEquals("foo", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
        iterator.set(Substring("modified"))
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("foo", iterator.next().token)
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("modified", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(2, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
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
        assertEquals("foo", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
        iterator.add(Substring("added"))
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.nextIndex())
        assertEquals("foo", iterator.next().token)
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("added", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(2, iterator2.nextIndex())
        assertEquals("bar", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(3, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
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
        assertEquals("foo", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
        iterator.remove()
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("foo", iterator.next().token)
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
        assertFalse(iterator2.hasNext())
        assertEquals(-1, iterator2.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator2.next() }
    }

    @Test
    fun testSequenceIteratorMergeWithPreceedingToken() {
        val token1 = Substring("0", positions = listOf(0))
        val token2 = Substring("1", positions = listOf(1))
        val token3 = Substring("2", positions = listOf(3))
        val token4 = Substring("3", positions = listOf(4))
        val token5 = Substring("4", positions = listOf(5))
        val tokens = mutableListOf(token1, token2, token3, token4, token5)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()

        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals("0", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("1", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("2", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.nextIndex())
        assertEquals("3", iterator.next().token)
        iterator.merge(1) { tokenToRetain, tokenToMerge ->
            tokenToRetain.token = "${tokenToMerge.token},${tokenToRetain.token}"
            tokenToRetain
        }
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.nextIndex())
        assertEquals("4", iterator.next().token)
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("0", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("2", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(2, iterator2.nextIndex())
        assertEquals("1,3", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(3, iterator2.nextIndex())
        assertEquals("4", iterator2.next().token)
        assertFalse(iterator2.hasNext())
        assertEquals(-1, iterator2.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator2.next() }
    }

    @Test
    fun testSequenceIteratorMergeWithSubsequentToken() {
        val token1 = Substring("0", positions = listOf(0))
        val token2 = Substring("1", positions = listOf(1))
        val token3 = Substring("2", positions = listOf(3))
        val token4 = Substring("3", positions = listOf(4))
        val token5 = Substring("4", positions = listOf(5))
        val tokens = mutableListOf(token1, token2, token3, token4, token5)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()

        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals("0", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("1", iterator.next().token)
        iterator.merge(3) { tokenToRetain, tokenToMerge ->
            tokenToRetain.token = "${tokenToRetain.token},${tokenToMerge.token}"
            tokenToRetain
        }
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("2", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.nextIndex())
        assertEquals("4", iterator.next().token)
        assertFalse(iterator.hasNext())
        assertEquals(-1, iterator.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator.next() }

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("0", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("1,3", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(2, iterator2.nextIndex())
        assertEquals("2", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(3, iterator2.nextIndex())
        assertEquals("4", iterator2.next().token)
        assertFalse(iterator2.hasNext())
        assertEquals(-1, iterator2.nextIndex())
        assertFailsWith(NoSuchElementException::class) { iterator2.next() }
    }

    @Test
    fun testSequenceIteratorSplit() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()

        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals("foo", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
        iterator.split { Pair(Substring("b"), Substring("ar")) }
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.nextIndex())
        assertEquals("ar", iterator.next().token)
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.nextIndex())
        assertEquals("foo", iterator.next().token)

        val iterator2 = sequence.sequenceIterator()
        assertTrue(iterator2.hasNext())
        assertEquals(0, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(1, iterator2.nextIndex())
        assertEquals("b", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(2, iterator2.nextIndex())
        assertEquals("ar", iterator2.next().token)
        assertTrue(iterator2.hasNext())
        assertEquals(3, iterator2.nextIndex())
        assertEquals("foo", iterator2.next().token)
    }

    @Test
    fun testConcurrentModificationException() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator1 = sequence.sequenceIterator()
        val iterator2 = sequence.sequenceIterator()
        iterator2.next()
        iterator2.set(Substring("modified"))
        assertFailsWith(ConcurrentModificationException::class) { iterator1.next() }
    }

    @Test
    fun testFindNext1() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()
        val result = iterator.findNext({ token -> token.token == "foo" })
        assertTrue(result)
        assertEquals(0, iterator.nextIndex())
        assertEquals("foo", iterator.next().token)
    }

    @Test
    fun testFindNext2() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()
        val result = iterator.findNext({ token -> token.token == "bar" })
        assertTrue(result)
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
    }

    @Test
    fun testFindNextIfMatchingTokenNotFound() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()
        val result = iterator.findNext({ token -> token.token == "xxx" })
        assertFalse(result)
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testFindNextIfMaxStepsSpecified() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()
        val result = iterator.findNext({ token -> token.token == "bar" }, 2)
        assertTrue(result)
        assertEquals(1, iterator.nextIndex())
        assertEquals("bar", iterator.next().token)
    }

    @Test
    fun testFindNextIfMaxStepsSpecifiedAndMatchingTokenNotFound() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator()
        val result = iterator.findNext({ token -> token.token == "bar" }, 1)
        assertFalse(result)
        assertEquals(1, iterator.nextIndex())
    }


    @Test
    fun testFindPrevious1() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator(sequence.size)
        val result = iterator.findPrevious({ token -> token.token == "foo" })
        assertTrue(result)
        assertEquals(2, iterator.previousIndex())
        assertEquals("foo", iterator.previous().token)
    }

    @Test
    fun testFindPrevious2() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator(sequence.size)
        val result = iterator.findPrevious({ token -> token.token == "bar" })
        assertTrue(result)
        assertEquals(1, iterator.previousIndex())
        assertEquals("bar", iterator.previous().token)
    }

    @Test
    fun testFindPreviousIfMatchingTokenNotFound() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator(sequence.size)
        val result = iterator.findPrevious({ token -> token.token == "xxx" })
        assertFalse(result)
        assertFalse(iterator.hasPrevious())
    }

    @Test
    fun testFindPreviousIfMaxStepsSpecified() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator(sequence.size)
        val result = iterator.findPrevious({ token -> token.token == "bar" }, 2)
        assertTrue(result)
        assertEquals(1, iterator.previousIndex())
        assertEquals("bar", iterator.previous().token)
    }

    @Test
    fun testFindPreviousIfMaxStepsSpecifiedAndMatchingTokenNotFound() {
        val token1 = Substring("foo", positions = listOf(0, 2))
        val token2 = Substring("bar", positions = listOf(1))
        val tokens = mutableListOf(token1, token2)
        val sequence = TokenSequence.createSorted(tokens)
        val iterator = sequence.sequenceIterator(sequence.size)
        val result = iterator.findPrevious({ token -> token.token == "bar" }, 1)
        assertFalse(result)
        assertEquals(1, iterator.previousIndex())
    }

}
