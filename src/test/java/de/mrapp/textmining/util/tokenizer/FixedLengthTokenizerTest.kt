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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlin.test.Test
import kotlin.test.fail

/**
 * Tests the functionality of the class [FixedLengthTokenizerTest].
 *
 * @author Michael Rapp
 */
class FixedLengthTokenizerTest {

    @Test
    fun testConstructor() {
        val length = 2
        val fixedLengthTokenizer = FixedLengthTokenizer(length)
        assertEquals(fixedLengthTokenizer.length, length)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorThrowsException() {
        FixedLengthTokenizer(0)
    }

    @Test
    fun testTokenize1() {
        val length = 2
        val substrings = FixedLengthTokenizer(length).tokenize("wirk")
        assertEquals(2, substrings.size)

        for (substring in substrings) {
            val positions = substring.positions

            when (substring.token) {
                "wi" -> assertTrue(positions.contains(0))
                "rk" -> assertTrue(positions.contains(2))
                else -> fail()
            }
        }
    }

    @Test
    fun testTokenizeThrowsExceptionIfTextIsEmpty() {
        val tokens = FixedLengthTokenizer(2).tokenize("")
        assertTrue(tokens.isEmpty())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTokenizeThrowsExceptionIfTextLengthIsInvalid() {
        FixedLengthTokenizer(2).tokenize("foo")
    }

}
