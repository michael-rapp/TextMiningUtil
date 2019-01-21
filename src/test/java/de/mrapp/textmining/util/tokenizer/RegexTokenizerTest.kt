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

import org.junit.Assert.*
import java.util.*
import java.util.regex.Pattern
import kotlin.test.Test

/**
 * Tests the functionality of the class [RegexTokenizer].
 *
 * @author Michael Rapp
 */
class RegexTokenizerTest {

    @Test
    fun testConstructorWithPatternArgument() {
        val pattern = Pattern.compile("\\s+")
        val regexTokenizer = RegexTokenizer(pattern)
        assertEquals(regexTokenizer.pattern, pattern)
    }

    @Test
    fun testConstructorWithStringArgument() {
        val regex = "\\s+"
        val regexTokenizer = RegexTokenizer(regex)
        assertEquals(regexTokenizer.pattern.toString(), regex)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorWithStringArgumentThrowsExceptionIfStringIsEmpty() {
        RegexTokenizer("")
    }

    @Test
    fun testTokenize1() {
        val substrings = RegexTokenizer(",").tokenize("foo")
        assertEquals(1, substrings.size)

        for (substring in substrings) {
            when (substring.token) {
                "foo" -> assertTrue(substring.positions.contains(0))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenize2() {
        val substrings = RegexTokenizer(",").tokenize("foo,bar")
        assertEquals(2, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions
            when (substring.token) {
                "foo" -> assertTrue(positions.contains(0))
                "bar" -> assertTrue(positions.contains(4))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenize3() {
        val substrings = RegexTokenizer(",").tokenize("foo,bar,foobar")
        assertEquals(3, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions

            when (substring.token) {
                "foo" -> assertTrue(positions.contains(0))
                "bar" -> assertTrue(positions.contains(4))
                "foobar" -> assertTrue(positions.contains(8))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenizeThrowsExceptionIfTextIsEmpty() {
        val tokens = RegexTokenizer(",").tokenize("")
        assertTrue(tokens.isEmpty())
    }

    @Test
    fun testSplitByWhitespace() {
        val regexTokenizer = RegexTokenizer.splitByWhitespace()
        assertEquals(regexTokenizer.pattern.toString(), "\\s+")
        val substrings = regexTokenizer.tokenize("foo  bar\nfoobar")
        assertEquals(3, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions
            when (substring.token) {
                "foo" -> assertTrue(positions.contains(0))
                "bar" -> assertTrue(positions.contains(5))
                "foobar" -> assertTrue(positions.contains(9))
                else -> fail()
            }
        }
    }

    @Test
    fun testSplitByDelimitersWithArrayArgument() {
        val regexTokenizer = RegexTokenizer.splitByDelimiters(",", ";")
        val substrings = regexTokenizer.tokenize("foo,bar;foobar")
        assertEquals(regexTokenizer.pattern.toString(), ",|;")
        assertEquals(3, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions
            when (substring.token) {
                "foo" -> assertTrue(positions.contains(0))
                "bar" -> assertTrue(positions.contains(4))
                "foobar" -> assertTrue(positions.contains(8))
                else -> fail()
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSplitByDelimitersWithArrayArgumentThrowsExceptionIfArrayIsEmpty() {
        RegexTokenizer.splitByDelimiters()
    }

    @Test
    fun testSplitByDelimitersWithIterableArgument() {
        val iterable = LinkedList<String>()
        iterable.add(",")
        iterable.add(";")
        val regexTokenizer = RegexTokenizer.splitByDelimiters(iterable)
        val substrings = regexTokenizer.tokenize("foo,bar;foobar")
        assertEquals(regexTokenizer.pattern.toString(), ",|;")
        assertEquals(3, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions

            when (substring.token) {
                "foo" -> assertTrue(positions.contains(0))
                "bar" -> assertTrue(positions.contains(4))
                "foobar" -> assertTrue(positions.contains(8))
                else -> fail()
            }
        }
    }

    @Test(expected = RuntimeException::class)
    fun testSplitByDelimitersWithIterableArgumentThrowsExceptionIfIterableIsNull() {
        val iterable: Iterable<String>? = null
        RegexTokenizer.splitByDelimiters(iterable!!)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSplitByDelimitersWithIterableArgumentThrowsExceptionIfIterableIsEmpty() {
        RegexTokenizer.splitByDelimiters(emptyList())
    }

}
