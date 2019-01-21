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
import kotlin.test.Test

/**
 * Tests the functionality of the class [SubstringTokenizer].
 *
 * @author Michael Rapp
 */
class SubstringTokenizerTest {

    @Test
    fun testDefaultConstructor() {
        val substringTokenizer = SubstringTokenizer()
        assertEquals(1, substringTokenizer.minLength)
        assertEquals(Integer.MAX_VALUE, substringTokenizer.maxLength)
    }

    @Test
    fun testConstructorWithMinAndMaxLengthParameters() {
        val minLength = 2
        val maxLength = 3
        val substringTokenizer = SubstringTokenizer(minLength, maxLength)
        assertEquals(minLength, substringTokenizer.minLength)
        assertEquals(maxLength, substringTokenizer.maxLength)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsLessThanOne() {
        SubstringTokenizer(0, 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfMaxLengthIsLessThanMinLength() {
        SubstringTokenizer(2, 1)
    }

    @Test
    fun testTokenize() {
        val substrings = SubstringTokenizer().tokenize("text")
        assertEquals(8, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions
            when (substring.token) {
                "t" -> {
                    assertTrue(positions.contains(0))
                    assertTrue(positions.contains(3))
                }
                "e" -> assertTrue(positions.contains(1))
                "x" -> assertTrue(positions.contains(2))
                "te" -> assertTrue(positions.contains(0))
                "ex" -> assertTrue(positions.contains(1))
                "xt" -> assertTrue(positions.contains(2))
                "tex" -> assertTrue(positions.contains(0))
                "ext" -> assertTrue(positions.contains(1))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenizeThrowsExceptionIfTextIsEmpty() {
        val tokens = SubstringTokenizer().tokenize("")
        assertTrue(tokens.isEmpty())
    }

    @Test
    fun testTokenizeWithMinAndMaxLength1() {
        val substrings = SubstringTokenizer(2, 3).tokenize("text")
        assertEquals(5, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions
            when (substring.token) {
                "te" -> assertTrue(positions.contains(0))
                "ex" -> assertTrue(positions.contains(1))
                "xt" -> assertTrue(positions.contains(2))
                "tex" -> assertTrue(positions.contains(0))
                "ext" -> assertTrue(positions.contains(1))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenizeWithMinAndMaxLength2() {
        val substrings = SubstringTokenizer(2, 2).tokenize("text")
        assertEquals(3, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions
            when (substring.token) {
                "te" -> assertTrue(positions.contains(0))
                "ex" -> assertTrue(positions.contains(1))
                "xt" -> assertTrue(positions.contains(2))
                else -> fail()
            }
        }
    }

}