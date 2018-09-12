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
import kotlin.test.*

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

    @Test
    fun testTranslate() {
        val dictionary = Dictionary<CharSequence, Int>()
        dictionary.addEntry(Dictionary.Entry("one", 1))
        dictionary.addEntry(Dictionary.Entry("two", 2))
        dictionary.addEntry(Dictionary.Entry("three", 3))
        val token1 = MutableToken(Substring("one"))
        val token2 = MutableToken(Substring("two"))
        val token3 = MutableToken(Substring("three"))
        val token4 = MutableToken(Substring("four"))
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.translate(dictionary)
        val result = processor.process(sequence)
        val iterator = result.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.next().getCurrent<ValueToken<Int>>().value)
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.next().getCurrent<ValueToken<Int>>().value)
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.next().getCurrent<ValueToken<Int>>().value)
        assertTrue(iterator.hasNext())
        assertEquals("four", iterator.next().getCurrent<Substring>().getToken())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testTranslateUsingMatcher() {
        val dictionary = Dictionary<CharSequence, Int>()
        dictionary.addEntry(Dictionary.Entry("one", 1))
        dictionary.addEntry(Dictionary.Entry("two", 2))
        dictionary.addEntry(Dictionary.Entry("three", 3))
        val matcher = Matcher.equals<CharSequence, CharSequence>()
        val token1 = MutableToken(Substring("one"))
        val token2 = MutableToken(Substring("two"))
        val token3 = MutableToken(Substring("three"))
        val token4 = MutableToken(Substring("four"))
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.translate(dictionary, matcher)
        val result = processor.process(sequence)
        val iterator = result.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(1, iterator.next().getCurrent<ValueToken<Int>>().value)
        assertTrue(iterator.hasNext())
        assertEquals(2, iterator.next().getCurrent<ValueToken<Int>>().value)
        assertTrue(iterator.hasNext())
        assertEquals(3, iterator.next().getCurrent<ValueToken<Int>>().value)
        assertTrue(iterator.hasNext())
        assertEquals("four", iterator.next().getCurrent<Substring>().getToken())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testRemove() {
        val token1 = Substring("one")
        val token2 = Substring("two")
        val token3 = Substring("three")
        val token4 = Substring("four")
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.remove<Substring> { token -> token.getToken().startsWith("t") }
        val result = processor.process(sequence)
        val iterator = result.iterator()
        assertTrue(iterator.hasNext())
        assertEquals("one", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals("four", iterator.next().getToken())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testRemoveUsingMatcher() {
        val token1 = Substring("one")
        val token2 = Substring("two")
        val token3 = Substring("three")
        val token4 = Substring("four")
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.remove<CharSequence, Substring>("t", Matcher.startsWith())
        val result = processor.process(sequence)
        val iterator = result.iterator()
        assertTrue(iterator.hasNext())
        assertEquals("one", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals("four", iterator.next().getToken())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testRetain() {
        val token1 = Substring("one")
        val token2 = Substring("two")
        val token3 = Substring("three")
        val token4 = Substring("four")
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.retain<Substring> { token -> token.getToken().startsWith("t") }
        val result = processor.process(sequence)
        val iterator = result.iterator()
        assertTrue(iterator.hasNext())
        assertEquals("two", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals("three", iterator.next().getToken())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testRetainUsingMatcher() {
        val token1 = Substring("one")
        val token2 = Substring("two")
        val token3 = Substring("three")
        val token4 = Substring("four")
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.retain<CharSequence, Substring>("t", Matcher.startsWith())
        val result = processor.process(sequence)
        val iterator = result.iterator()
        assertTrue(iterator.hasNext())
        assertEquals("two", iterator.next().getToken())
        assertTrue(iterator.hasNext())
        assertEquals("three", iterator.next().getToken())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testEnsureAllMatch() {
        val token1 = Substring("one")
        val token2 = Substring("two")
        val token3 = Substring("three")
        val token4 = Substring("four")
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.ensureAllMatch<Substring>(predicate = { token ->
            token.getToken().isNotEmpty()
        })
        assertEquals(sequence, processor.process(sequence))
        val processor2 = Processor.ensureAllMatch<Substring>(predicate = { token ->
            token.getToken().startsWith("t")
        })
        assertFailsWith(MalformedTextException::class) { processor2.process(sequence) }
    }

    @Test
    fun testEnsureAnyMatch() {
        val token1 = Substring("one")
        val token2 = Substring("two")
        val token3 = Substring("three")
        val token4 = Substring("four")
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.ensureAnyMatch<Substring>(predicate = { token ->
            token.getToken().startsWith("t")
        })
        assertEquals(sequence, processor.process(sequence))
        val processor2 = Processor.ensureAnyMatch<Substring>(predicate = { token ->
            token.getToken().startsWith("x")
        })
        assertFailsWith(MalformedTextException::class) { processor2.process(sequence) }
    }

    @Test
    fun testEnsureNoneMatch() {
        val token1 = Substring("one")
        val token2 = Substring("two")
        val token3 = Substring("three")
        val token4 = Substring("four")
        val sequence = TokenSequence(mutableListOf(token1, token2, token3, token4))
        val processor = Processor.ensureNoneMatch<Substring>(predicate = { token ->
            token.getToken().startsWith("x")
        })
        assertEquals(sequence, processor.process(sequence))
        val processor2 = Processor.ensureNoneMatch<Substring>(predicate = { token ->
            token.getToken().startsWith("t")
        })
        assertFailsWith(MalformedTextException::class) { processor2.process(sequence) }
    }

}
