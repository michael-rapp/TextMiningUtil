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
package de.mrapp.textmining.util.tokenizer

import org.junit.Assert.*
import kotlin.test.Test

/**
 * Tests the functionality of the class [NGramTokenizer].
 *
 * @author Michael Rapp
 */
class NGramTokenizerTest {

    @Test
    fun testDefaultConstructor() {
        val nGramTokenizer = NGramTokenizer()
        assertEquals(1, nGramTokenizer.minLength)
        assertEquals(Int.MAX_VALUE, nGramTokenizer.maxLength)
    }

    @Test
    fun testConstructorWithMinLengthParameter() {
        val minLength = 3
        val nGramTokenizer = NGramTokenizer(minLength)
        assertEquals(minLength, nGramTokenizer.minLength)
        assertEquals(Int.MAX_VALUE, nGramTokenizer.maxLength)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTokenizeWithMinLengthParameterThrowsExceptionIfMinLengthIsLessThanOne() {
        NGramTokenizer(0)
    }

    @Test
    fun testConstructorWithMinAndMaxLengthParameters() {
        val minLength = 2
        val maxLength = 3
        val nGramTokenizer = NGramTokenizer(minLength, maxLength)
        assertEquals(minLength, nGramTokenizer.minLength)
        assertEquals(maxLength, nGramTokenizer.maxLength)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsLessThanOne() {
        NGramTokenizer(0, 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfMaxLengthIsLessThanMinLength() {
        NGramTokenizer(2, 1)
    }

    @Test
    fun testTokenize1() {
        val nGrams = NGramTokenizer().tokenize("wirk")
        assertEquals(6, nGrams.size)

        for ((_, token, positions) in nGrams) {
            when (token) {
                "w" -> assertTrue(positions.contains(0))
                "wi" -> assertTrue(positions.contains(0))
                "wir" -> assertTrue(positions.contains(0))
                "irk" -> assertTrue(positions.contains(1))
                "rk" -> assertTrue(positions.contains(2))
                "k" -> assertTrue(positions.contains(3))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenize2() {
        val nGrams = NGramTokenizer().tokenize("wi")
        assertEquals(2, nGrams.size)

        for ((_, token, positions) in nGrams) {
            when (token) {
                "w" -> assertTrue(positions.contains(0))
                "i" -> assertTrue(positions.contains(1))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenize3() {
        val nGrams = NGramTokenizer().tokenize("wir")
        assertEquals(4, nGrams.size)

        for ((_, token, positions) in nGrams) {
            when (token) {
                "w" -> assertTrue(positions.contains(0))
                "wi" -> assertTrue(positions.contains(0))
                "ir" -> assertTrue(positions.contains(1))
                "r" -> assertTrue(positions.contains(2))
                else -> fail()
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTokenizeThrowsExceptionIfTextIsEmpty() {
        NGramTokenizer().tokenize("")
    }

    @Test
    fun testTokenizeWithMinAndMaxLength1() {
        val nGrams = NGramTokenizer(2, 3).tokenize("wirk")
        assertEquals(4, nGrams.size)

        for ((n, token, positions) in nGrams) {
            assertEquals(n, 3)

            when (token) {
                "wi" -> assertTrue(positions.contains(0))
                "wir" -> assertTrue(positions.contains(0))
                "irk" -> assertTrue(positions.contains(1))
                "rk" -> assertTrue(positions.contains(2))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenizeWithMinAndMaxLength2() {
        val nGrams = NGramTokenizer(1, 2).tokenize("wirk")
        assertEquals(5, nGrams.size)

        for ((n, token, positions) in nGrams) {
            assertEquals(n, 2)

            when (token) {
                "w" -> assertTrue(positions.contains(0))
                "wi" -> assertTrue(positions.contains(0))
                "ir" -> assertTrue(positions.contains(1))
                "rk" -> assertTrue(positions.contains(2))
                "k" -> assertTrue(positions.contains(3))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenizeWithMinAndMaxLength3() {
        val nGrams = NGramTokenizer(2, 2).tokenize("wirk")
        assertEquals(3, nGrams.size)

        for ((n, token, positions) in nGrams) {
            assertEquals(n, 2)

            when (token) {
                "wi" -> assertTrue(positions.contains(0))
                "ir" -> assertTrue(positions.contains(1))
                "rk" -> assertTrue(positions.contains(2))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenizeWithMinAndMaxLength4() {
        val nGrams = NGramTokenizer(1, 1).tokenize("text")
        assertEquals(3, nGrams.size)

        for ((n, token, positions) in nGrams) {
            assertEquals(n, 1)

            when (token) {
                "t" -> {
                    assertTrue(positions.contains(0))
                    assertTrue(positions.contains(3))
                }
                "e" -> assertTrue(positions.contains(1))
                "x" -> assertTrue(positions.contains(2))
                else -> fail()
            }
        }
    }

}